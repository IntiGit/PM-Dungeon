package starter;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static logging.LoggerConfig.initBaseLogger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import configuration.Configuration;
import configuration.KeyboardConfig;
import controller.AbstractController;
import controller.SystemController;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.components.xp.XPComponent;
import ecs.entities.Entity;
import ecs.entities.Geist;
import ecs.entities.Grabstein;
import ecs.entities.Hero;
import ecs.entities.monsters.*;
import ecs.entities.traps.Loch;
import ecs.entities.traps.Schleim;
import ecs.items.*;
import ecs.systems.*;
import graphic.DungeonCamera;
import graphic.Painter;
import graphic.hud.GameOverMenu;
import graphic.hud.PauseMenu;
import graphic.hud.Questanzeige;
import graphic.hud.statDisplay.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import level.IOnLevelLoader;
import level.LevelAPI;
import level.elements.ILevel;
import level.elements.tile.Tile;
import level.generator.IGenerator;
import level.generator.postGeneration.WallGenerator;
import level.generator.randomwalk.RandomWalkGenerator;
import level.tools.LevelSize;
import quests.KillAllMonstersQuest;
import quests.KillMonsterQuest;
import quests.Quest;
import tools.Constants;
import tools.Point;

/** The heart of the framework. From here all strings are pulled. */
public class Game extends ScreenAdapter implements IOnLevelLoader {

    private final LevelSize LEVELSIZE = LevelSize.MEDIUM;

    /**
     * The batch is necessary to draw ALL the stuff. Every object that uses draw need to know the
     * batch.
     */
    protected SpriteBatch batch;

    /** Contains all Controller of the Dungeon */
    protected List<AbstractController<?>> controller;

    public static DungeonCamera camera;
    /** Draws objects */
    protected Painter painter;

    protected LevelAPI levelAPI;
    /** Generates the level */
    protected IGenerator generator;

    private boolean doSetup = true;
    private static boolean paused = false;
    private static boolean playerDied = false;
    /** Variable um anzuzeigen ob gerade das Inventar offen ist */
    public static boolean inventoryOpen = false;
    /** Variable um anzuzeigen ob gerade eine Tasche offen ist */
    public static boolean bagOpen = false;

    public static boolean questScreenOpen = false;

    /** All entities that are currently active in the dungeon */
    private static final Set<Entity> entities = new HashSet<>();
    /** All entities to be removed from the dungeon in the next frame */
    private static final Set<Entity> entitiesToRemove = new HashSet<>();
    /** All entities to be added from the dungeon in the next frame */
    private static final Set<Entity> entitiesToAdd = new HashSet<>();

    /** List of all Systems in the ECS */
    public static SystemController systems;

    // HUD Elemente
    private static PauseMenu<Actor> pauseMenu;
    private static GameOverMenu<Actor> gameover;
    private static Lebensanzeige<Actor> lebensanzeige;
    private static MonsterLebensanzeige<Actor> monsterLebensanzeige;
    private static Levelanzeige<Actor> levelanzeige;
    private static Skillanzeige<Actor> skillanzeige;
    private static Inventaranzeige<Actor> inventaranzeige;
    public static Questanzeige<Actor> questanzeige;

    public static ILevel currentLevel;
    private static Entity hero;
    private Logger gameLogger;
    private Scanner sc = new Scanner(System.in);

    public static float difficulty = 1f;

    public static boolean hasGhost = false;
    private int levelCount = 0;
    private boolean levelJustLoaded = false;

    public static ItemFactory itemFactory = new ItemFactory();

    public static void main(String[] args) {
        // start the game
        try {
            Configuration.loadAndGetConfiguration("dungeon_config.json", KeyboardConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DesktopLauncher.run(new Game());
    }

    /**
     * Main game loop. Redraws the dungeon and calls the own implementation (beginFrame, endFrame
     * and onLevelLoad).
     *
     * @param delta Time since last loop.
     */
    @Override
    public void render(float delta) {
        if (doSetup) setup();
        batch.setProjectionMatrix(camera.combined);
        frame();
        clearScreen();
        levelAPI.update();
        controller.forEach(AbstractController::update);
        camera.update();
    }

    /** Called once at the beginning of the game. */
    protected void setup() {
        doSetup = false;
        controller = new ArrayList<>();
        setupCameras();
        painter = new Painter(batch, camera);
        generator = new RandomWalkGenerator();
        levelAPI = new LevelAPI(batch, painter, generator, this);
        initBaseLogger();
        gameLogger = Logger.getLogger(this.getClass().getName());
        systems = new SystemController();
        controller.add(systems);
        hero = new Hero();
        levelAPI = new LevelAPI(batch, painter, new WallGenerator(new RandomWalkGenerator()), this);
        levelAPI.loadLevel(LEVELSIZE);
        createSystems();
        // HUD Elemente
        pauseMenu = new PauseMenu<>();
        gameover = new GameOverMenu<>();
        lebensanzeige = new Lebensanzeige<>();
        monsterLebensanzeige = new MonsterLebensanzeige<>();
        levelanzeige = new Levelanzeige<>();
        skillanzeige = new Skillanzeige<>();
        inventaranzeige = new Inventaranzeige<>();
        questanzeige = new Questanzeige<>();

        controller.add(pauseMenu);
        controller.add(gameover);
        controller.add(lebensanzeige);
        controller.add(monsterLebensanzeige);
        controller.add(levelanzeige);
        controller.add(skillanzeige);
        controller.add(inventaranzeige);
        controller.add(questanzeige);

        if (hero instanceof Hero h) {
            h.register(lebensanzeige);
            h.register(levelanzeige);
            h.register(skillanzeige);
            h.register(inventaranzeige);
            h.notifyObservers();
            questanzeige.update(h);
        }
    }

    /** Called at the beginning of each frame. Before the controllers call <code>update</code>. */
    protected void frame() {
        setCameraFocus();
        manageEntitiesSets();
        monsterLebensanzeige.update(hero);
        getHero().ifPresent(this::loadNextLevelIfEntityIsOnEndTile);
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) togglePause();
        if (Gdx.input.isKeyJustPressed(KeyboardConfig.INVENTORY_OPEN.get())) toggleInventory();
        if (inventoryOpen && bagOpen) {
            if (Gdx.input.isKeyJustPressed(KeyboardConfig.INVENTORY_NAVIGATE_UP.get())) {
                inventaranzeige.selectNextItemVertical();
            }
            if (Gdx.input.isKeyJustPressed(KeyboardConfig.INVENTORY_NAVIGATE_DOWN.get())) {
                inventaranzeige.selectPreviousItemVertical();
            }
            if (Gdx.input.isKeyJustPressed(KeyboardConfig.INVENTORY_USE_ITEM.get())) {
                inventaranzeige.useItem();
            }
        } else if (inventoryOpen) {
            if (Gdx.input.isKeyJustPressed(KeyboardConfig.INVENTORY_NAVIGATE_RIGHT.get())) {
                inventaranzeige.selectNextItemHorizontal();
            }
            if (Gdx.input.isKeyJustPressed(KeyboardConfig.INVENTORY_NAVIGATE_LEFT.get())) {
                inventaranzeige.selectPreviousItemHorizontal();
            }
            if (Gdx.input.isKeyJustPressed(KeyboardConfig.INVENTORY_USE_ITEM.get())) {
                inventaranzeige.useItem();
            }
        }
        if (playerDied && Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            System.exit(0);
        }
        if (playerDied && Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            playerDied = false;
            gameover.hideMenu();
            setHero(new Hero());
            if (hero instanceof Hero h) {
                h.register(lebensanzeige);
                h.register(levelanzeige);
                h.register(skillanzeige);
                h.register(inventaranzeige);
                h.notifyObservers();
            }
            levelAPI.loadLevel(LEVELSIZE);
        }
        if (questScreenOpen && Gdx.input.isKeyJustPressed(Input.Keys.X)) questScreenOpen = false;
        if (questScreenOpen && Gdx.input.isKeyJustPressed(Input.Keys.U)) questanzeige.acceptQuest();


    }

    @Override
    public void onLevelLoad() {
        levelJustLoaded = true;
        levelCount++;
        currentLevel = levelAPI.getCurrentLevel();
        entities.clear();
        getHero().ifPresent(this::placeOnLevelStart);

        generateMonsters();
        difficulty += 0.1;

        generateTraps();

        generateItems();

        if (levelCount % 5 == 0) {
            hasGhost = true;
        } else {
            hasGhost = false;
        }
        generateGhost();

        XPComponent heroXPCom = (XPComponent) hero.getComponent(XPComponent.class).orElseThrow();
        heroXPCom.addXP(50);
        gameLogger.info(
                "\nXP: " + heroXPCom.getCurrentXP() + "\n" + "LVL: " + heroXPCom.getCurrentLevel());
    }

    private void manageEntitiesSets() {
        for(Entity e : entitiesToRemove) {
            if(e instanceof Monster m) {
                Hero h = (Hero) getHero().get();
                List<Quest> heroQuests = h.getMyQuests();
                for (Quest q : heroQuests) {
                    if (q instanceof KillMonsterQuest kmQ) {
                        kmQ.addToKillcount(m);
                    }
                }
            }
        }


        entities.removeAll(entitiesToRemove);
        entities.addAll(entitiesToAdd);
        for (Entity entity : entitiesToRemove) {
            gameLogger.info("Entity '" + entity.getClass().getSimpleName() + "' was deleted.");
        }
        for (Entity entity : entitiesToAdd) {
            gameLogger.info("Entity '" + entity.getClass().getSimpleName() + "' was added.");
        }
        entitiesToRemove.clear();
        entitiesToAdd.clear();

        if(levelJustLoaded) {
            Hero h = (Hero) getHero().get();
            List<Quest> heroQuests = h.getMyQuests();
            for (Quest q : heroQuests) {
                if (q instanceof KillAllMonstersQuest kamQ) {
                    kamQ.setMonsterSet(entities);
                }
            }
            levelJustLoaded = false;
        }
    }

    private void setCameraFocus() {
        if (getHero().isPresent()) {
            PositionComponent pc =
                    (PositionComponent)
                            getHero()
                                    .get()
                                    .getComponent(PositionComponent.class)
                                    .orElseThrow(
                                            () ->
                                                    new MissingComponentException(
                                                            "PositionComponent"));
            camera.setFocusPoint(pc.getPosition());

        } else camera.setFocusPoint(new Point(0, 0));
    }

    private void loadNextLevelIfEntityIsOnEndTile(Entity hero) {
        if (isOnEndTile(hero)) levelAPI.loadLevel(LEVELSIZE);
    }

    private boolean isOnEndTile(Entity entity) {
        PositionComponent pc =
                (PositionComponent)
                        entity.getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () -> new MissingComponentException("PositionComponent"));
        Tile currentTile = currentLevel.getTileAt(pc.getPosition().toCoordinate());
        return currentTile.equals(currentLevel.getEndTile());
    }

    private void placeOnLevelStart(Entity hero) {
        entities.add(hero);
        PositionComponent pc =
                (PositionComponent)
                        hero.getComponent(PositionComponent.class)
                                .orElseThrow(
                                        () -> new MissingComponentException("PositionComponent"));
        pc.setPosition(currentLevel.getStartTile().getCoordinate().toPoint());
    }

    /** Toggle between pause and run */
    public static void togglePause() {
        paused = !paused;
        if (systems != null) {
            systems.forEach(ECS_System::toggleRun);
        }
        if (pauseMenu != null) {
            if (paused) pauseMenu.showMenu();
            else pauseMenu.hideMenu();
        }
    }

    private void toggleInventory() {
        inventoryOpen = !inventoryOpen;
        if (systems != null) {
            systems.forEach(ECS_System::toggleRun);
        }
        if (inventoryOpen) {
            lebensanzeige.hideMenu();
            monsterLebensanzeige.hideMenu();
            levelanzeige.hideMenu();
            skillanzeige.hideMenu();

            inventaranzeige.showMenu();
        } else {
            lebensanzeige.showMenu();
            monsterLebensanzeige.showMenu();
            levelanzeige.showMenu();
            skillanzeige.showMenu();

            inventaranzeige.hideMenu();
        }
    }

    /** Zeigt GameOverMenü an und setzt setzt Schwierigkeit zurück */
    public static void activateGameOver() {
        gameover.showMenu();
        playerDied = true;
        difficulty = 1.0f;
    }

    /**
     * Given entity will be added to the game in the next frame
     *
     * @param entity will be added to the game next frame
     */
    public static void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
    }

    /**
     * Given entity will be removed from the game in the next frame
     *
     * @param entity will be removed from the game next frame
     */
    public static void removeEntity(Entity entity) {
        entitiesToRemove.add(entity);
    }

    /**
     * @return Set with all entities currently in game
     */
    public static Set<Entity> getEntities() {
        return entities;
    }

    /**
     * @return Set with all entities that will be added to the game next frame
     */
    public static Set<Entity> getEntitiesToAdd() {
        return entitiesToAdd;
    }

    /**
     * @return Set with all entities that will be removed from the game next frame
     */
    public static Set<Entity> getEntitiesToRemove() {
        return entitiesToRemove;
    }

    /**
     * @return the player character, can be null if not initialized
     */
    public static Optional<Entity> getHero() {
        return Optional.ofNullable(hero);
    }

    /**
     * set the reference of the playable character careful: old hero will not be removed from the
     * game
     *
     * @param hero new reference of hero
     */
    public static void setHero(Entity hero) {
        Game.hero = hero;
    }

    public void setSpriteBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void setupCameras() {
        camera = new DungeonCamera(null, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.zoom = Constants.DEFAULT_ZOOM_FACTOR;

        // See also:
        // https://stackoverflow.com/questions/52011592/libgdx-set-ortho-camera
    }

    private void createSystems() {
        new VelocitySystem();
        new DrawSystem(painter);
        new PlayerSystem();
        new AISystem();
        new CollisionSystem();
        new HealthSystem();
        new XPSystem();
        new SkillSystem();
        new ProjectileSystem();
    }

    private void generateMonsters() {
        entities.add(new ChestMonster(itemFactory.getRandomItem()));
        Random randomMons = new Random();
        int monsterAmount =
                (int) Math.floor(randomMons.nextFloat(3 * difficulty, 5.1f * difficulty));
        while (monsterAmount > 0) {

            double randomNum = Math.random();

            if (randomNum >= 0.66) {
                entities.add(new Skelett());
                monsterAmount--;
            } else if (randomNum >= 0.33 && difficulty > 1.5) {
                entities.add(new Daemon());
                monsterAmount--;
            } else if (randomNum >= 0.0 && difficulty > 2.0) {
                entities.add(new Necromancer());
                monsterAmount--;
            }
        }
    }

    private void generateTraps() {
        Random trapAmount = new Random();
        int amount = trapAmount.nextInt(2, 5);

        while (amount > 0) {
            double randomNum = Math.random();

            if (randomNum >= 0.5) {
                entities.add(new Schleim(3));
            } else {
                entities.add(new Loch(0));
            }
            amount--;
        }
    }

    private void generateGhost() {
        if (hasGhost) {
            Geist geist = new Geist();
            Grabstein grabstein = new Grabstein(10, 20, 2);

            geist.setGrabstein(grabstein);
            grabstein.setGeist(geist);

            entities.add(geist);
            entities.add(grabstein);
        }
    }

    private void generateItems() {
        Random rng = new Random();
        int amount = rng.nextInt(4);
        while (amount != 0) {
            ItemData item = itemFactory.getRandomItem();
            entities.add(WorldItemBuilder.buildWorldItem(item));
            amount--;
        }
    }
}
