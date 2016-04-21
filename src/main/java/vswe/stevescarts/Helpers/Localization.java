package vswe.stevescarts.Helpers;

import net.minecraft.util.StatCollector;

public final class Localization
{
    private static String doTranslate(String name, String ... vars)
    {
        String result = StatCollector.translateToLocal(name);

        for (int i = 0; i < vars.length; ++i)
        {
            String pluralCheck = "[%" + (i + 1) + ":";
            int index = result.indexOf(pluralCheck);

            if (index != -1)
            {
                int listCheck = result.indexOf("]", index);

                if (listCheck != -1)
                {
                    String index2 = result.substring(index + pluralCheck.length(), listCheck);
                    String[] endIndex = index2.split("\\|");
                    int optionsStr = !vars[i].equals("1") && !vars[i].equals("-1") ? 1 : 0;

                    if (optionsStr >= 0 && optionsStr < endIndex.length)
                    {
                        String options = endIndex[optionsStr];
                        result = result.substring(0, index) + options + result.substring(listCheck + 1);
                        --i;
                    }
                }
            }
            else
            {
                String var13 = "[%" + (i + 1) + "->";
                int var14 = result.indexOf(var13);

                if (var14 != -1)
                {
                    int var15 = result.indexOf("]", var14);

                    if (var15 != -1)
                    {
                        String var16 = result.substring(var14 + var13.length(), var15);
                        String[] var17 = var16.split("\\|");
                        int optionId = Integer.parseInt(vars[i]);

                        if (optionId >= 0 && optionId < var17.length)
                        {
                            String option = var17[optionId];
                            result = result.substring(0, var14) + option + result.substring(var15 + 1);
                            --i;
                        }
                    }
                }
                else
                {
                    result = result.replace("[%" + (i + 1) + "]", vars[i]);
                }
            }
        }

        return result;
    }

    public static enum ARCADE
    {
        GHAST("GHAST", 0, "ghastInvaders"),
        EXTRA_LIVES("EXTRA_LIVES", 1, "ghastLives"),
        HIGH_SCORE("HIGH_SCORE", 2, "highScore"),
        SCORE("SCORE", 3, "score"),
        INSTRUCTION_SHOOT("INSTRUCTION_SHOOT", 4, "instructionShoot"),
        INSTRUCTION_LEFT("INSTRUCTION_LEFT", 5, "instructionLeft"),
        INSTRUCTION_RIGHT("INSTRUCTION_RIGHT", 6, "instructionRight"),
        INSTRUCTION_RESTART("INSTRUCTION_RESTART", 7, "instructionRestart"),
        CREEPER("CREEPER", 8, "creeperSweeper"),
        MAP_1("MAP_1", 9, "creeperMapName1"),
        MAP_2("MAP_2", 10, "creeperMapName2"),
        MAP_3("MAP_3", 11, "creeperMapName3"),
        LEFT("LEFT", 12, "creepersLeft"),
        TIME("TIME", 13, "creeperTime"),
        INSTRUCTION_CHANGE_MAP("INSTRUCTION_CHANGE_MAP", 14, "instructionChangeMap"),
        MAP("MAP", 15, "creeperCurrentMap"),
        HIGH_SCORES("HIGH_SCORES", 16, "creeperHighScores"),
        HIGH_SCORE_ENTRY("HIGH_SCORE_ENTRY", 17, "creeperHighScore"),
        STACKER("STACKER", 18, "mobStacker"),
        REMOVED_LINES("REMOVED_LINES", 19, "stackerRemovedLines"),
        REMOVED_LINES_COMBO("REMOVED_LINES_COMBO", 20, "stackerRemovedLinesCombo"),
        INSTRUCTION_ROTATE("INSTRUCTION_ROTATE", 21, "instructionRotate"),
        INSTRUCTION_DROP("INSTRUCTION_DROP", 22, "instructionDrop"),
        OPERATOR("OPERATOR", 23, "trackOperator"),
        SAVE_ERROR("SAVE_ERROR", 24, "operatorSaveError"),
        SAVE("SAVE", 25, "operatorSave"),
        USER_MAPS("USER_MAPS", 26, "operatorUserCreatedMaps"),
        STORIES("STORIES", 27, "operatorStories"),
        HELP("HELP", 28, "operatorHelp"),
        INSTRUCTION_SHAPE("INSTRUCTION_SHAPE", 29, "instructionTrackShape"),
        INSTRUCTION_ROTATE_TRACK("INSTRUCTION_ROTATE_TRACK", 30, "instructionRotateTrack"),
        INSTRUCTION_FLIP_TRACK("INSTRUCTION_FLIP_TRACK", 31, "instructionFlipTrack"),
        INSTRUCTION_DEFAULT_DIRECTION("INSTRUCTION_DEFAULT_DIRECTION", 32, "instructionDefaultDirection"),
        INSTRUCTION_TRACK_TYPE("INSTRUCTION_TRACK_TYPE", 33, "instructionTrackType"),
        INSTRUCTION_DELETE_TRACK("INSTRUCTION_DELETE_TRACK", 34, "instructionDeleteTrack"),
        INSTRUCTION_COPY_TRACK("INSTRUCTION_COPY_TRACK", 35, "instructionCopyTrack"),
        INSTRUCTION_STEVE("INSTRUCTION_STEVE", 36, "instructionMoveSteve"),
        INSTRUCTION_MAP("INSTRUCTION_MAP", 37, "instructionMoveMap"),
        INSTRUCTION_PLACE_TRACK("INSTRUCTION_PLACE_TRACK", 38, "instructionPlaceTrack"),
        INSTRUCTION_DESELECT_TRACK("INSTRUCTION_DESELECT_TRACK", 39, "instructionDeselectTrack"),
        LEFT_MOUSE("LEFT_MOUSE", 40, "leftMouseButton"),
        RIGHT_MOUSE("RIGHT_MOUSE", 41, "rightMouseButton"),
        BUTTON_START("BUTTON_START", 42, "buttonStart"),
        BUTTON_MENU("BUTTON_MENU", 43, "buttonMenu"),
        BUTTON_STOP("BUTTON_STOP", 44, "buttonStop"),
        BUTTON_NEXT("BUTTON_NEXT", 45, "buttonNextLevel"),
        BUTTON_START_LEVEL("BUTTON_START_LEVEL", 46, "buttonStartLevel"),
        BUTTON_SELECT_STORY("BUTTON_SELECT_STORY", 47, "buttonSelectStory"),
        BUTTON_SELECT_OTHER_STORY("BUTTON_SELECT_OTHER_STORY", 48, "buttonSelectStoryOther"),
        BUTTON_CREATE_LEVEL("BUTTON_CREATE_LEVEL", 49, "buttonCreateLevel"),
        BUTTON_EDIT_LEVEL("BUTTON_EDIT_LEVEL", 50, "buttonEditLevel"),
        BUTTON_REFRESH("BUTTON_REFRESH", 51, "buttonRefreshList"),
        BUTTON_SAVE("BUTTON_SAVE", 52, "buttonSave"),
        BUTTON_SAVE_AS("BUTTON_SAVE_AS", 53, "buttonSaveAs"),
        BUTTON_CANCEL("BUTTON_CANCEL", 54, "buttonCancel"),
        MADNESS("MADNESS", 55, "forgecraftMadness");
        private String name;

        private static final Localization.ARCADE[] $VALUES = new Localization.ARCADE[]{GHAST, EXTRA_LIVES, HIGH_SCORE, SCORE, INSTRUCTION_SHOOT, INSTRUCTION_LEFT, INSTRUCTION_RIGHT, INSTRUCTION_RESTART, CREEPER, MAP_1, MAP_2, MAP_3, LEFT, TIME, INSTRUCTION_CHANGE_MAP, MAP, HIGH_SCORES, HIGH_SCORE_ENTRY, STACKER, REMOVED_LINES, REMOVED_LINES_COMBO, INSTRUCTION_ROTATE, INSTRUCTION_DROP, OPERATOR, SAVE_ERROR, SAVE, USER_MAPS, STORIES, HELP, INSTRUCTION_SHAPE, INSTRUCTION_ROTATE_TRACK, INSTRUCTION_FLIP_TRACK, INSTRUCTION_DEFAULT_DIRECTION, INSTRUCTION_TRACK_TYPE, INSTRUCTION_DELETE_TRACK, INSTRUCTION_COPY_TRACK, INSTRUCTION_STEVE, INSTRUCTION_MAP, INSTRUCTION_PLACE_TRACK, INSTRUCTION_DESELECT_TRACK, LEFT_MOUSE, RIGHT_MOUSE, BUTTON_START, BUTTON_MENU, BUTTON_STOP, BUTTON_NEXT, BUTTON_START_LEVEL, BUTTON_SELECT_STORY, BUTTON_SELECT_OTHER_STORY, BUTTON_CREATE_LEVEL, BUTTON_EDIT_LEVEL, BUTTON_REFRESH, BUTTON_SAVE, BUTTON_SAVE_AS, BUTTON_CANCEL, MADNESS};

        private ARCADE(String var1, int var2, String name)
        {
            this.name = name;
        }

        public String translate(String ... vars)
        {
            return Localization.doTranslate("arcade.SC2:" + this.name, vars);
        }
    }

    public static class GUI
    {
        public static enum ASSEMBLER
        {
            TITLE("TITLE", 0, "cartAssembler"),
            ASSEMBLE_INSTRUCTION("ASSEMBLE_INSTRUCTION", 1, "basicAssembleInstruction"),
            INVALID_HULL("INVALID_HULL", 2, "invalidHullError"),
            HULL_CAPACITY("HULL_CAPACITY", 3, "hullCapacity"),
            COMPLEXITY_CAP("COMPLEXITY_CAP", 4, "complexityCap"),
            TOTAL_COST("TOTAL_COST", 5, "totalCost"),
            TOTAl_TIME("TOTAl_TIME", 6, "totalTime"),
            NO_ERROR("NO_ERROR", 7, "readyMessage"),
            ASSEMBLE_PROGRESS("ASSEMBLE_PROGRESS", 8, "assembleProgress"),
            TIME_LEFT("TIME_LEFT", 9, "timeLeft"),
            IDLE_MESSAGE("IDLE_MESSAGE", 10, "idleAssemblerMessage"),
            MODIFY_CART("MODIFY_CART", 11, "modifyCart"),
            ASSEMBLE_CART("ASSEMBLE_CART", 12, "assembleCart"),
            FUEL_LEVEL("FUEL_LEVEL", 13, "fuelLevel"),
            HULL_ERROR("HULL_ERROR", 14, "noHullError"),
            INVALID_HULL_SHORT("INVALID_HULL_SHORT", 15, "invalidHullErrorShort"),
            BUSY("BUSY", 16, "busyAssemblerError"),
            DEPARTURE_BAY("DEPARTURE_BAY", 17, "departureBayError");
            private String name;

            private static final Localization.GUI.ASSEMBLER[] $VALUES = new Localization.GUI.ASSEMBLER[]{TITLE, ASSEMBLE_INSTRUCTION, INVALID_HULL, HULL_CAPACITY, COMPLEXITY_CAP, TOTAL_COST, TOTAl_TIME, NO_ERROR, ASSEMBLE_PROGRESS, TIME_LEFT, IDLE_MESSAGE, MODIFY_CART, ASSEMBLE_CART, FUEL_LEVEL, HULL_ERROR, INVALID_HULL_SHORT, BUSY, DEPARTURE_BAY};

            private ASSEMBLER(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("gui.SC2:" + this.name, vars);
            }
        }

        public static enum CARGO
        {
            TITLE("TITLE", 0, "cargoManager"),
            CHANGE_SLOT_LAYOUT("CHANGE_SLOT_LAYOUT", 1, "changeSlotLayout"),
            LAYOUT_SHARED("LAYOUT_SHARED", 2, "layoutShared"),
            LAYOUT_SIDE("LAYOUT_SIDE", 3, "layoutSide"),
            LAYOUT_COLOR("LAYOUT_COLOR", 4, "layoutColor"),
            TRANSFER_ALL("TRANSFER_ALL", 5, "transferAll"),
            TRANSFER_ITEMS("TRANSFER_ITEMS", 6, "transferItems"),
            TRANSFER_STACKS("TRANSFER_STACKS", 7, "transferStacks"),
            TRANSFER_ALL_SHORT("TRANSFER_ALL_SHORT", 8, "transferAllShort"),
            TRANSFER_ITEMS_SHORT("TRANSFER_ITEMS_SHORT", 9, "transferItemsShort"),
            TRANSFER_STACKS_SHORT("TRANSFER_STACKS_SHORT", 10, "transferStacksShort"),
            CHANGE_STORAGE_AREA("CHANGE_STORAGE_AREA", 11, "changeTransferCartArea"),
            UNKNOWN_AREA("UNKNOWN_AREA", 12, "unknownAreaMessage"),
            AREA_ALL("AREA_ALL", 13, "cartAreaAll"),
            AREA_ENGINE("AREA_ENGINE", 14, "cartAreaEngine"),
            AREA_RAILER("AREA_RAILER", 15, "cartAreaRailer"),
            AREA_STORAGE("AREA_STORAGE", 16, "cartAreaStorage"),
            AREA_TORCHES("AREA_TORCHES", 17, "cartAreaTorches"),
            AREA_EXPLOSIVES("AREA_EXPLOSIVES", 18, "cartAreaExplosives"),
            AREA_ARROWS("AREA_ARROWS", 19, "cartAreaArrows"),
            AREA_BRIDGE("AREA_BRIDGE", 20, "cartAreaBridge"),
            AREA_SEEDS("AREA_SEEDS", 21, "cartAreaSeeds"),
            AREA_FERTILIZER("AREA_FERTILIZER", 22, "cartAreaFertilizer"),
            AREA_SAPLINGS("AREA_SAPLINGS", 23, "cartAreaSaplings"),
            AREA_FIREWORK("AREA_FIREWORK", 24, "cartAreaFirework"),
            AREA_BUCKETS("AREA_BUCKETS", 25, "cartAreaBuckets"),
            AREA_CAKES("AREA_CAKES", 26, "cartAreaCakes");
            private String name;

            private static final Localization.GUI.CARGO[] $VALUES = new Localization.GUI.CARGO[]{TITLE, CHANGE_SLOT_LAYOUT, LAYOUT_SHARED, LAYOUT_SIDE, LAYOUT_COLOR, TRANSFER_ALL, TRANSFER_ITEMS, TRANSFER_STACKS, TRANSFER_ALL_SHORT, TRANSFER_ITEMS_SHORT, TRANSFER_STACKS_SHORT, CHANGE_STORAGE_AREA, UNKNOWN_AREA, AREA_ALL, AREA_ENGINE, AREA_RAILER, AREA_STORAGE, AREA_TORCHES, AREA_EXPLOSIVES, AREA_ARROWS, AREA_BRIDGE, AREA_SEEDS, AREA_FERTILIZER, AREA_SAPLINGS, AREA_FIREWORK, AREA_BUCKETS, AREA_CAKES};

            private CARGO(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("gui.SC2:" + this.name, vars);
            }
        }

        public static enum DETECTOR
        {
            OUTPUT("OUTPUT", 0, "operatorOutput"),
            AND("AND", 1, "operatorAnd"),
            OR("OR", 2, "operatorOr"),
            NOT("NOT", 3, "operatorNot"),
            XOR("XOR", 4, "operatorXor"),
            TOP("TOP", 5, "operatorTopUnit"),
            BOT("BOT", 6, "operatorBotUnit"),
            NORTH("NORTH", 7, "operatorNorthUnit"),
            WEST("WEST", 8, "operatorWestUnit"),
            SOUTH("SOUTH", 9, "operatorSouthUnit"),
            EAST("EAST", 10, "operatorEastUnit"),
            REDSTONE("REDSTONE", 11, "operatorRedstone"),
            REDSTONE_TOP("REDSTONE_TOP", 12, "operatorRedstoneTop"),
            REDSTONE_BOT("REDSTONE_BOT", 13, "operatorRedstoneBot"),
            REDSTONE_NORTH("REDSTONE_NORTH", 14, "operatorRedstoneNorth"),
            REDSTONE_WEST("REDSTONE_WEST", 15, "operatorRedstoneWest"),
            REDSTONE_SOUTH("REDSTONE_SOUTH", 16, "operatorRedstoneSouth"),
            REDSTONE_EAST("REDSTONE_EAST", 17, "operatorRedstoneEast"),
            RAIL("RAIL", 18, "stateRails"),
            TORCH("TORCH", 19, "stateTorches"),
            SAPLING("SAPLING", 20, "stateSaplings"),
            SEED("SEED", 21, "sateSeeds"),
            BRIDGE("BRIDGE", 22, "stateBridge"),
            PROJECTILE("PROJECTILE", 23, "stateProjectiles"),
            FERTILIZING("FERTILIZING", 24, "stateFertilizing"),
            SHIELD("SHIELD", 25, "stateShield"),
            CHUNK("CHUNK", 26, "stateChunk"),
            INVISIBILITY("INVISIBILITY", 27, "stateInvisibility"),
            DRILL("DRILL", 28, "stateDrill"),
            CAGE("CAGE", 29, "stateCage"),
            STORAGE_FULL("STORAGE_FULL", 30, "stateStorageFull"),
            STORAGE_EMPTY("STORAGE_EMPTY", 31, "stateStorageEmpty"),
            PASSENGER("PASSENGER", 32, "statePassenger"),
            ANIMAL("ANIMAL", 33, "stateAnimal"),
            TAMEABLE("TAMEABLE", 34, "stateTameable"),
            BREEDABLE("BREEDABLE", 35, "stateBreedable"),
            HOSTILE("HOSTILE", 36, "stateHostile"),
            CREEPER("CREEPER", 37, "stateCreeper"),
            SKELETON("SKELETON", 38, "stateSkeleton"),
            SPIDER("SPIDER", 39, "stateSpider"),
            ZOMBIE("ZOMBIE", 40, "stateZombie"),
            PIG_MAN("PIG_MAN", 41, "stateZombiePigMan"),
            SILVERFISH("SILVERFISH", 42, "stateSilverFish"),
            BLAZE("BLAZE", 43, "stateBlaze"),
            BAT("BAT", 44, "stateBat"),
            WITCH("WITCH", 45, "stateWitch"),
            PIG("PIG", 46, "statePig"),
            SHEEP("SHEEP", 47, "stateSheep"),
            COW("COW", 48, "stateCow"),
            MOOSHROOM("MOOSHROOM", 49, "stateMooshroom"),
            CHICKEN("CHICKEN", 50, "stateChicken"),
            WOLF("WOLF", 51, "stateWolf"),
            SNOW_GOLEM("SNOW_GOLEM", 52, "stateSnowGolem"),
            OCELOT("OCELOT", 53, "stateOcelot"),
            VILLAGER("VILLAGER", 54, "stateVillager"),
            PLAYER("PLAYER", 55, "statePlayer"),
            ZOMBIE_VILLAGER("ZOMBIE_VILLAGER", 56, "stateZombieVillager"),
            CHILD("CHILD", 57, "stateChild"),
            TAMED("TAMED", 58, "stateTamed"),
            POWER_RED("POWER_RED", 59, "statePowerRed"),
            POWER_BLUE("POWER_BLUE", 60, "statePowerBlue"),
            POWER_GREEN("POWER_GREEN", 61, "statePowerGreen"),
            POWER_YELLOW("POWER_YELLOW", 62, "statePowerYellow"),
            TANKS_FULL("TANKS_FULL", 63, "stateTanksFull"),
            TANKS_EMPTY("TANKS_EMPTY", 64, "stateTanksEmpty"),
            TANK_EMPTY("TANK_EMPTY", 65, "stateTankEmpty"),
            CAKE("CAKE", 66, "stateCake");
            private String name;

            private static final Localization.GUI.DETECTOR[] $VALUES = new Localization.GUI.DETECTOR[]{OUTPUT, AND, OR, NOT, XOR, TOP, BOT, NORTH, WEST, SOUTH, EAST, REDSTONE, REDSTONE_TOP, REDSTONE_BOT, REDSTONE_NORTH, REDSTONE_WEST, REDSTONE_SOUTH, REDSTONE_EAST, RAIL, TORCH, SAPLING, SEED, BRIDGE, PROJECTILE, FERTILIZING, SHIELD, CHUNK, INVISIBILITY, DRILL, CAGE, STORAGE_FULL, STORAGE_EMPTY, PASSENGER, ANIMAL, TAMEABLE, BREEDABLE, HOSTILE, CREEPER, SKELETON, SPIDER, ZOMBIE, PIG_MAN, SILVERFISH, BLAZE, BAT, WITCH, PIG, SHEEP, COW, MOOSHROOM, CHICKEN, WOLF, SNOW_GOLEM, OCELOT, VILLAGER, PLAYER, ZOMBIE_VILLAGER, CHILD, TAMED, POWER_RED, POWER_BLUE, POWER_GREEN, POWER_YELLOW, TANKS_FULL, TANKS_EMPTY, TANK_EMPTY, CAKE};

            private DETECTOR(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("gui.SC2:" + this.name, vars);
            }
        }

        public static enum DISTRIBUTOR
        {
            TITLE("TITLE", 0, "externalDistributor"),
            NOT_CONNECTED("NOT_CONNECTED", 1, "distributorNotConnected"),
            SIDE("SIDE", 2, "sideName"),
            DROP_INSTRUCTION("DROP_INSTRUCTION", 3, "dropInstruction"),
            REMOVE_INSTRUCTION("REMOVE_INSTRUCTION", 4, "removeInstruction"),
            SETTING_ALL("SETTING_ALL", 5, "distributorAll"),
            SETTING_RED("SETTING_RED", 6, "distributorRed"),
            SETTING_BLUE("SETTING_BLUE", 7, "distributorBlue"),
            SETTING_YELLOW("SETTING_YELLOW", 8, "distributorYellow"),
            SETTING_GREEN("SETTING_GREEN", 9, "distributorGreen"),
            SETTING_TOP_LEFT("SETTING_TOP_LEFT", 10, "distributorTopLeft"),
            SETTING_TOP_RIGHT("SETTING_TOP_RIGHT", 11, "distributorTopRight"),
            SETTING_BOTTOM_LEFT("SETTING_BOTTOM_LEFT", 12, "distributorBottomLeft"),
            SETTING_BOTTOM_RIGHT("SETTING_BOTTOM_RIGHT", 13, "distributorBottomRight"),
            SETTING_TO_CART("SETTING_TO_CART", 14, "distributorToCart"),
            SETTING_FROM_CART("SETTING_FROM_CART", 15, "distributorFromCart"),
            MANAGER_TOP("MANAGER_TOP", 16, "managerTop"),
            MANAGER_BOT("MANAGER_BOT", 17, "managerBot"),
            SIDE_ORANGE("SIDE_ORANGE", 18, "distributorSideOrange"),
            SIDE_PURPLE("SIDE_PURPLE", 19, "distributorSidePurple"),
            SIDE_YELLOW("SIDE_YELLOW", 20, "distributorSideYellow"),
            SIDE_GREEN("SIDE_GREEN", 21, "distributorSideGreen"),
            SIDE_BLUE("SIDE_BLUE", 22, "distributorSideBlue"),
            SIDE_RED("SIDE_RED", 23, "distributorSideRed"),
            SIDE_TOOL_TIP("SIDE_TOOL_TIP", 24, "sideToolTip");
            private String name;

            private static final Localization.GUI.DISTRIBUTOR[] $VALUES = new Localization.GUI.DISTRIBUTOR[]{TITLE, NOT_CONNECTED, SIDE, DROP_INSTRUCTION, REMOVE_INSTRUCTION, SETTING_ALL, SETTING_RED, SETTING_BLUE, SETTING_YELLOW, SETTING_GREEN, SETTING_TOP_LEFT, SETTING_TOP_RIGHT, SETTING_BOTTOM_LEFT, SETTING_BOTTOM_RIGHT, SETTING_TO_CART, SETTING_FROM_CART, MANAGER_TOP, MANAGER_BOT, SIDE_ORANGE, SIDE_PURPLE, SIDE_YELLOW, SIDE_GREEN, SIDE_BLUE, SIDE_RED, SIDE_TOOL_TIP};

            private DISTRIBUTOR(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("gui.SC2:" + this.name, vars);
            }
        }

        public static enum LIQUID
        {
            TITLE("TITLE", 0, "liquidManager"),
            CHANGE_LAYOUT("CHANGE_LAYOUT", 1, "changeTankLayout"),
            LAYOUT_ALL("LAYOUT_ALL", 2, "layoutSharedTanks"),
            LAYOUT_SIDE("LAYOUT_SIDE", 3, "layoutSidedTanks"),
            LAYOUT_COLOR("LAYOUT_COLOR", 4, "layoutColorTanks"),
            TRANSFER_ALL("TRANSFER_ALL", 5, "transferAllLiquid"),
            TRANSFER_BUCKETS("TRANSFER_BUCKETS", 6, "transferBuckets"),
            TRANSFER_ALL_SHORT("TRANSFER_ALL_SHORT", 7, "transferAllLiquidShort"),
            TRANSFER_BUCKET_SHORT("TRANSFER_BUCKET_SHORT", 8, "transferBucketShort");
            private String name;

            private static final Localization.GUI.LIQUID[] $VALUES = new Localization.GUI.LIQUID[]{TITLE, CHANGE_LAYOUT, LAYOUT_ALL, LAYOUT_SIDE, LAYOUT_COLOR, TRANSFER_ALL, TRANSFER_BUCKETS, TRANSFER_ALL_SHORT, TRANSFER_BUCKET_SHORT};

            private LIQUID(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("gui.SC2:" + this.name, vars);
            }
        }

        public static enum MANAGER
        {
            TITLE("TITLE", 0, "manager"),
            CURRENT_SETTING("CURRENT_SETTING", 1, "currentSetting"),
            CHANGE_TRANSFER_DIRECTION("CHANGE_TRANSFER_DIRECTION", 2, "changeTransferDirection"),
            DIRECTION_TO_CART("DIRECTION_TO_CART", 3, "directionToCart"),
            DIRECTION_FROM_CART("DIRECTION_FROM_CART", 4, "directionFromCart"),
            CHANGE_TURN_BACK_SETTING("CHANGE_TURN_BACK_SETTING", 5, "changeTurnBack"),
            TURN_BACK_NOT_SELECTED("TURN_BACK_NOT_SELECTED", 6, "turnBackDisabled"),
            TURN_BACK_DO("TURN_BACK_DO", 7, "turnBack"),
            TURN_BACK_DO_NOT("TURN_BACK_DO_NOT", 8, "continueForward"),
            CHANGE_TRANSFER_SIZE("CHANGE_TRANSFER_SIZE", 9, "changeTransferSize"),
            CHANGE_SIDE("CHANGE_SIDE", 10, "changeSide"),
            CURRENT_SIDE("CURRENT_SIDE", 11, "currentSide"),
            SIDE_RED("SIDE_RED", 12, "sideRed"),
            SIDE_BLUE("SIDE_BLUE", 13, "sideBlue"),
            SIDE_YELLOW("SIDE_YELLOW", 14, "sideYellow"),
            SIDE_GREEN("SIDE_GREEN", 15, "sideGreen"),
            SIDE_DISABLED("SIDE_DISABLED", 16, "sideDisabled");
            private String name;

            private static final Localization.GUI.MANAGER[] $VALUES = new Localization.GUI.MANAGER[]{TITLE, CURRENT_SETTING, CHANGE_TRANSFER_DIRECTION, DIRECTION_TO_CART, DIRECTION_FROM_CART, CHANGE_TURN_BACK_SETTING, TURN_BACK_NOT_SELECTED, TURN_BACK_DO, TURN_BACK_DO_NOT, CHANGE_TRANSFER_SIZE, CHANGE_SIDE, CURRENT_SIDE, SIDE_RED, SIDE_BLUE, SIDE_YELLOW, SIDE_GREEN, SIDE_DISABLED};

            private MANAGER(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("gui.SC2:" + this.name, vars);
            }
        }

        public static enum TOGGLER
        {
            TITLE("TITLE", 0, "moduleToggler"),
            OPTION_DRILL("OPTION_DRILL", 1, "optionDrill"),
            OPTION_SHIELD("OPTION_SHIELD", 2, "optionShield"),
            OPTION_INVISIBILITY("OPTION_INVISIBILITY", 3, "optionInvisibility"),
            OPTION_CHUNK("OPTION_CHUNK", 4, "optionChunk"),
            OPTION_CAGE_AUTO("OPTION_CAGE_AUTO", 5, "optionCageAuto"),
            OPTION_CAGE("OPTION_CAGE", 6, "optionCage"),
            SETTING_DISABLED("SETTING_DISABLED", 7, "settingDisabled"),
            SETTING_ORANGE("SETTING_ORANGE", 8, "settingOrange"),
            SETTING_BLUE("SETTING_BLUE", 9, "settingBlue"),
            STATE_ACTIVATE("STATE_ACTIVATE", 10, "stateActivate"),
            STATE_DEACTIVATE("STATE_DEACTIVATE", 11, "stateDeactivate"),
            STATE_TOGGLE("STATE_TOGGLE", 12, "stateToggle");
            private String name;

            private static final Localization.GUI.TOGGLER[] $VALUES = new Localization.GUI.TOGGLER[]{TITLE, OPTION_DRILL, OPTION_SHIELD, OPTION_INVISIBILITY, OPTION_CHUNK, OPTION_CAGE_AUTO, OPTION_CAGE, SETTING_DISABLED, SETTING_ORANGE, SETTING_BLUE, STATE_ACTIVATE, STATE_DEACTIVATE, STATE_TOGGLE};

            private TOGGLER(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("gui.SC2:" + this.name, vars);
            }
        }
    }

    public static class MODULES
    {
        public static enum ADDONS
        {
            BUTTON_RANDOMIZE("BUTTON_RANDOMIZE", 0, "buttonRandomize"),
            DETECTOR_ANIMALS("DETECTOR_ANIMALS", 1, "detectorAnimals"),
            DETECTOR_BATS("DETECTOR_BATS", 2, "detectorBats"),
            DETECTOR_MONSTERS("DETECTOR_MONSTERS", 3, "detectorMonsters"),
            DETECTOR_PLAYERS("DETECTOR_PLAYERS", 4, "detectorPlayers"),
            DETECTOR_VILLAGERS("DETECTOR_VILLAGERS", 5, "detectorVillagers"),
            PLANTER_RANGE("PLANTER_RANGE", 6, "planterRangeExtenderTitle"),
            SAPLING_AMOUNT("SAPLING_AMOUNT", 7, "saplingPlantAmount"),
            CONTROL_LEVER("CONTROL_LEVER", 8, "controlLeverTitle"),
            LEVER_START("LEVER_START", 9, "leverStartCart"),
            LEVER_STOP("LEVER_STOP", 10, "leverStopCart"),
            LEVER_TURN("LEVER_TURN", 11, "leverTurnAroundCart"),
            COLOR_RED("COLOR_RED", 12, "colorizerRgbRed"),
            COLOR_GREEN("COLOR_GREEN", 13, "colorizerRgbGreen"),
            COLOR_BLUE("COLOR_BLUE", 14, "colorizerRgbBlue"),
            LOCKED("LOCKED", 15, "intelligenceLockedBlock"),
            CHANGE_INTELLIGENCE("CHANGE_INTELLIGENCE", 16, "intelligenceChange"),
            CURRENT_INTELLIGENCE("CURRENT_INTELLIGENCE", 17, "intelligenceCurrent"),
            ENCHANT_INSTRUCTION("ENCHANT_INSTRUCTION", 18, "enchanterInstruction"),
            INVISIBILITY("INVISIBILITY", 19, "invisibilityToggle"),
            NAME("NAME", 20, "informationProviderLabelName"),
            DISTANCE("DISTANCE", 21, "informationProviderLabelDistance"),
            DISTANCE_LONG("DISTANCE_LONG", 22, "informationProviderMessageDistance"),
            POSITION("POSITION", 23, "informationProviderLabelPosition"),
            POSITION_LONG("POSITION_LONG", 24, "informationProviderMessagePosition"),
            FUEL("FUEL", 25, "informationProviderLabelFuel"),
            FUEL_LONG("FUEL_LONG", 26, "informationProviderMessageFuel"),
            FUEL_NO_CONSUMPTION("FUEL_NO_CONSUMPTION", 27, "informationProviderMessageNoConsumption"),
            STORAGE("STORAGE", 28, "informationProviderLabelStorage"),
            LABELS("LABELS", 29, "informationProviderLabels"),
            DURABILITY("DURABILITY", 30, "informationProviderLabelDurability"),
            BROKEN("BROKEN", 31, "informationProviderMessageToolBroken"),
            NOT_BROKEN("NOT_BROKEN", 32, "informationProviderMessageToolNotBroken"),
            REPAIR("REPAIR", 33, "informationProviderMessageRepair"),
            UNBREAKABLE("UNBREAKABLE", 34, "informationProviderMessageUnbreakable"),
            K("K", 35, "powerThousandSuffix"),
            OBSERVER_INSTRUCTION("OBSERVER_INSTRUCTION", 36, "powerObserverInstruction"),
            OBSERVER_REMOVE("OBSERVER_REMOVE", 37, "powerObserverRemoveInstruction"),
            OBSERVER_DROP("OBSERVER_DROP", 38, "powerObserverDropInstruction"),
            OBSERVER_CHANGE("OBSERVER_CHANGE", 39, "powerObserverChangeInstruction"),
            OBSERVER_CHANGE_10("OBSERVER_CHANGE_10", 40, "powerObserverChangeInstruction10"),
            RECIPE_OUTPUT("RECIPE_OUTPUT", 41, "recipeOutput"),
            CURRENT("CURRENT", 42, "recipeCurrentSelection"),
            INVALID_OUTPUT("INVALID_OUTPUT", 43, "recipeInvalidOutput"),
            RECIPE_MODE("RECIPE_MODE", 44, "recipeChangeMode"),
            RECIPE_NO_LIMIT("RECIPE_NO_LIMIT", 45, "recipeNoLimit"),
            RECIPE_LIMIT("RECIPE_LIMIT", 46, "recipeLimit"),
            RECIPE_DISABLED("RECIPE_DISABLED", 47, "recipeDisabled"),
            RECIPE_CHANGE_AMOUNT("RECIPE_CHANGE_AMOUNT", 48, "recipeChangeLimit"),
            RECIPE_CHANGE_AMOUNT_10("RECIPE_CHANGE_AMOUNT_10", 49, "recipeChangeLimit10"),
            RECIPE_CHANGE_AMOUNT_64("RECIPE_CHANGE_AMOUNT_64", 50, "recipeChangeLimit64"),
            SHIELD("SHIELD", 51, "shieldToggle"),
            RETURNING_HOME("RETURNING_HOME", 52, "returningHome"),
            BLOCKED("BLOCKED", 53, "isBlocked"),
            CURRENT_TOOL("CURRENT_TOOL", 54, "currentTool");
            private String name;

            private static final Localization.MODULES.ADDONS[] $VALUES = new Localization.MODULES.ADDONS[]{BUTTON_RANDOMIZE, DETECTOR_ANIMALS, DETECTOR_BATS, DETECTOR_MONSTERS, DETECTOR_PLAYERS, DETECTOR_VILLAGERS, PLANTER_RANGE, SAPLING_AMOUNT, CONTROL_LEVER, LEVER_START, LEVER_STOP, LEVER_TURN, COLOR_RED, COLOR_GREEN, COLOR_BLUE, LOCKED, CHANGE_INTELLIGENCE, CURRENT_INTELLIGENCE, ENCHANT_INSTRUCTION, INVISIBILITY, NAME, DISTANCE, DISTANCE_LONG, POSITION, POSITION_LONG, FUEL, FUEL_LONG, FUEL_NO_CONSUMPTION, STORAGE, LABELS, DURABILITY, BROKEN, NOT_BROKEN, REPAIR, UNBREAKABLE, K, OBSERVER_INSTRUCTION, OBSERVER_REMOVE, OBSERVER_DROP, OBSERVER_CHANGE, OBSERVER_CHANGE_10, RECIPE_OUTPUT, CURRENT, INVALID_OUTPUT, RECIPE_MODE, RECIPE_NO_LIMIT, RECIPE_LIMIT, RECIPE_DISABLED, RECIPE_CHANGE_AMOUNT, RECIPE_CHANGE_AMOUNT_10, RECIPE_CHANGE_AMOUNT_64, SHIELD, RETURNING_HOME, BLOCKED, CURRENT_TOOL};

            private ADDONS(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("modules.addons.SC2:" + this.name, vars);
            }
        }

        public static enum ATTACHMENTS
        {
            FERTILIZERS("FERTILIZERS", 0, "fertilizers"),
            RAILER("RAILER", 1, "railerTitle"),
            CONTROL_SYSTEM("CONTROL_SYSTEM", 2, "controlSystemTitle"),
            DISTANCES("DISTANCES", 3, "controlSystemDistanceUnits"),
            ODO("ODO", 4, "controlSystemOdoMeter"),
            TRIP("TRIP", 5, "controlSystemTripMeter"),
            CAGE_AUTO("CAGE_AUTO", 6, "cageAutoPickUp"),
            CAGE("CAGE", 7, "cagePickUp"),
            CAKE_SERVER("CAKE_SERVER", 8, "cakeServerTitle"),
            CAKES("CAKES", 9, "cakesLabel"),
            SLICES("SLICES", 10, "slicesLabel"),
            EXPLOSIVES("EXPLOSIVES", 11, "explosivesTitle"),
            EXPERIENCE("EXPERIENCE", 12, "experienceTitle"),
            EXPERIENCE_LEVEL("EXPERIENCE_LEVEL", 13, "experienceLevel"),
            EXPERIENCE_EXTRACT("EXPERIENCE_EXTRACT", 14, "experienceExtract"),
            EXPERIENCE_PLAYER_LEVEL("EXPERIENCE_PLAYER_LEVEL", 15, "experiencePlayerLevel"),
            SHOOTER("SHOOTER", 16, "shooterTitle"),
            FREQUENCY("FREQUENCY", 17, "shooterFrequency"),
            SECONDS("SECONDS", 18, "shooterSeconds"),
            DELAY("DELAY", 19, "shooterDelay"),
            PIANO("PIANO", 20, "notePiano"),
            BASS_DRUM("BASS_DRUM", 21, "noteBassDrum"),
            SNARE_DRUM("SNARE_DRUM", 22, "noteSnareDrum"),
            STICKS("STICKS", 23, "noteSticks"),
            BASS_GUITAR("BASS_GUITAR", 24, "noteBassGuitar"),
            CREATE_TRACK("CREATE_TRACK", 25, "noteCreateTrack"),
            REMOVE_TRACK("REMOVE_TRACK", 26, "noteRemoveTrack"),
            ACTIVATE_INSTRUMENT("ACTIVATE_INSTRUMENT", 27, "noteActivateInstrument"),
            DEACTIVATE_INSTRUMENT("DEACTIVATE_INSTRUMENT", 28, "noteDeactivateInstrument"),
            NOTE_DELAY("NOTE_DELAY", 29, "noteDelay"),
            ADD_NOTE("ADD_NOTE", 30, "noteAdd"),
            REMOVE_NOTE("REMOVE_NOTE", 31, "noteRemove"),
            VOLUME("VOLUME", 32, "noteVolume"),
            SEAT_MESSAGE("SEAT_MESSAGE", 33, "seatStateMessage"),
            CONTROL_RESET("CONTROL_RESET", 34, "controlSystemReset");
            private String name;

            private static final Localization.MODULES.ATTACHMENTS[] $VALUES = new Localization.MODULES.ATTACHMENTS[]{FERTILIZERS, RAILER, CONTROL_SYSTEM, DISTANCES, ODO, TRIP, CAGE_AUTO, CAGE, CAKE_SERVER, CAKES, SLICES, EXPLOSIVES, EXPERIENCE, EXPERIENCE_LEVEL, EXPERIENCE_EXTRACT, EXPERIENCE_PLAYER_LEVEL, SHOOTER, FREQUENCY, SECONDS, DELAY, PIANO, BASS_DRUM, SNARE_DRUM, STICKS, BASS_GUITAR, CREATE_TRACK, REMOVE_TRACK, ACTIVATE_INSTRUMENT, DEACTIVATE_INSTRUMENT, NOTE_DELAY, ADD_NOTE, REMOVE_NOTE, VOLUME, SEAT_MESSAGE, CONTROL_RESET};

            private ATTACHMENTS(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("modules.attachments.SC2:" + this.name, vars);
            }
        }

        public static enum ENGINES
        {
            OVER_9000("OVER_9000", 0, "creativePowerLevel"),
            COAL("COAL", 1, "coalEngineTitle"),
            NO_FUEL("NO_FUEL", 2, "outOfFuel"),
            FUEL("FUEL", 3, "fuelLevel"),
            SOLAR("SOLAR", 4, "solarEngineTitle"),
            NO_POWER("NO_POWER", 5, "outOfPower"),
            POWER("POWER", 6, "powerLevel"),
            THERMAL("THERMAL", 7, "thermalEngineTitle"),
            POWERED("POWERED", 8, "thermalPowered"),
            NO_WATER("NO_WATER", 9, "outOfWater"),
            NO_LAVA("NO_LAVA", 10, "outOfLava"),
            ENGINE_DISABLED("ENGINE_DISABLED", 11, "engineDisabledMessage"),
            ENGINE_PRIORITY("ENGINE_PRIORITY", 12, "enginePriorityMessage"),
            RF("RF", 13, "rfEngineTitle");
            private String name;

            private static final Localization.MODULES.ENGINES[] $VALUES = new Localization.MODULES.ENGINES[]{OVER_9000, COAL, NO_FUEL, FUEL, SOLAR, NO_POWER, POWER, THERMAL, POWERED, NO_WATER, NO_LAVA, ENGINE_DISABLED, ENGINE_PRIORITY};

            private ENGINES(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("modules.engines.SC2:" + this.name, vars);
            }
        }

        public static enum TANKS
        {
            CREATIVE_MODE("CREATIVE_MODE", 0, "creativeTankMode"),
            CHANGE_MODE("CHANGE_MODE", 1, "creativeTankChangeMode"),
            RESET_MODE("RESET_MODE", 2, "creativeTankResetMode"),
            LOCKED("LOCKED", 3, "tankLocked"),
            LOCK("LOCK", 4, "tankLock"),
            UNLOCK("UNLOCK", 5, "tankUnlock"),
            EMPTY("EMPTY", 6, "tankEmpty"),
            INVALID("INVALID", 7, "tankInvalidFluid");
            private String name;

            private static final Localization.MODULES.TANKS[] $VALUES = new Localization.MODULES.TANKS[]{CREATIVE_MODE, CHANGE_MODE, RESET_MODE, LOCKED, LOCK, UNLOCK, EMPTY, INVALID};

            private TANKS(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("modules.tanks.SC2:" + this.name, vars);
            }
        }

        public static enum TOOLS
        {
            DURABILITY("DURABILITY", 0, "toolDurability"),
            BROKEN("BROKEN", 1, "toolBroken"),
            REPAIRING("REPAIRING", 2, "toolRepairing"),
            DECENT("DECENT", 3, "toolDecent"),
            INSTRUCTION("INSTRUCTION", 4, "toolRepairInstruction"),
            UNBREAKABLE("UNBREAKABLE", 5, "toolUnbreakable"),
            UNBREAKABLE_REPAIR("UNBREAKABLE_REPAIR", 6, "toolUnbreakableRepairError"),
            DRILL("DRILL", 7, "drillTitle"),
            TOGGLE("TOGGLE", 8, "drillToggle"),
            DIAMONDS("DIAMONDS", 9, "repairDiamonds"),
            IRON("IRON", 10, "repairIron"),
            FARMER("FARMER", 11, "farmerTitle"),
            CUTTER("CUTTER", 12, "cutterTitle");
            private String name;

            private static final Localization.MODULES.TOOLS[] $VALUES = new Localization.MODULES.TOOLS[]{DURABILITY, BROKEN, REPAIRING, DECENT, INSTRUCTION, UNBREAKABLE, UNBREAKABLE_REPAIR, DRILL, TOGGLE, DIAMONDS, IRON, FARMER, CUTTER};

            private TOOLS(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("modules.tools.SC2:" + this.name, vars);
            }
        }
    }

    public static enum MODULE_INFO
    {
        ENGINE_GROUP("ENGINE_GROUP", 0, "moduleGroupEngine"),
        DRILL_GROUP("DRILL_GROUP", 1, "moduleGroupDrill"),
        FARMER_GROUP("FARMER_GROUP", 2, "moduleGroupFarmer"),
        CUTTER_GROUP("CUTTER_GROUP", 3, "moduleGroupCutter"),
        TANK_GROUP("TANK_GROUP", 4, "moduleGroupTank"),
        ENTITY_GROUP("ENTITY_GROUP", 5, "moduleGroupEntity"),
        SHOOTER_GROUP("SHOOTER_GROUP", 6, "moduleGroupShooter"),
        TOOL_GROUP("TOOL_GROUP", 7, "moduleGroupTool"),
        TOOL_OR_SHOOTER_GROUP("TOOL_OR_SHOOTER_GROUP", 8, "moduleGroupToolShooter"),
        HULL_CATEGORY("HULL_CATEGORY", 9, "moduleCategoryHull"),
        ENGINE_CATEGORY("ENGINE_CATEGORY", 10, "moduleCategoryEngine"),
        TOOL_CATEGORY("TOOL_CATEGORY", 11, "moduleCategoryTool"),
        STORAGE_CATEGORY("STORAGE_CATEGORY", 12, "moduleCategoryStorage"),
        ADDON_CATEGORY("ADDON_CATEGORY", 13, "moduleCategoryAddon"),
        ATTACHMENT_CATEGORY("ATTACHMENT_CATEGORY", 14, "moduleCategoryAttachment"),
        PIG_MESSAGE("PIG_MESSAGE", 15, "pigExtraMessage"),
        OCEAN_MESSAGE("OCEAN_MESSAGE", 16, "oceanExtraMessage"),
        ALPHA_MESSAGE("ALPHA_MESSAGE", 17, "alphaExtraMessage"),
        STORAGE_EMPTY("STORAGE_EMPTY", 18, "storageEmpty"),
        STORAGE_FULL("STORAGE_FULL", 19, "storageFull"),
        GIFT_STORAGE_FULL("GIFT_STORAGE_FULL", 20, "giftStorageFull"),
        EGG_STORAGE_FULL("EGG_STORAGE_FULL", 21, "eggStorageFull"),
        MODULAR_COST("MODULAR_COST", 22, "modularCost"),
        SIDE_NONE("SIDE_NONE", 23, "cartSideNone"),
        SIDE_TOP("SIDE_TOP", 24, "cartSideTop"),
        SIDE_CENTER("SIDE_CENTER", 25, "cartSideCenter"),
        SIDE_BOTTOM("SIDE_BOTTOM", 26, "cartSideBottom"),
        SIDE_BACK("SIDE_BACK", 27, "cartSideBack"),
        SIDE_LEFT("SIDE_LEFT", 28, "cartSideLeft"),
        SIDE_RIGHT("SIDE_RIGHT", 29, "cartSideRight"),
        SIDE_FRONT("SIDE_FRONT", 30, "cartSideFront"),
        OCCUPIED_SIDES("OCCUPIED_SIDES", 31, "occupiedSides"),
        AND("AND", 32, "sidesAnd"),
        NO_SIDES("NO_SIDES", 33, "noSides"),
        CONFLICT_HOWEVER("CONFLICT_HOWEVER", 34, "moduleConflictHowever"),
        CONFLICT_ALSO("CONFLICT_ALSO", 35, "moduleConflictAlso"),
        REQUIREMENT("REQUIREMENT", 36, "moduleRequirement"),
        MODULE_COUNT_1("MODULE_COUNT_1", 37, "moduleCount1"),
        MODULE_COUNT_2("MODULE_COUNT_2", 38, "moduleCount2"),
        MODULE_COUNT_3("MODULE_COUNT_3", 39, "moduleCount3"),
        DUPLICATES("DUPLICATES", 40, "allowDuplicates"),
        TYPE("TYPE", 41, "moduleType"),
        CAPACITY_ERROR("CAPACITY_ERROR", 42, "capacityOverloadError"),
        COMBINATION_ERROR("COMBINATION_ERROR", 43, "impossibleCombinationError"),
        COMPLEXITY_ERROR("COMPLEXITY_ERROR", 44, "complexityOverloadError"),
        PARENT_ERROR("PARENT_ERROR", 45, "missingParentError"),
        NEMESIS_ERROR("NEMESIS_ERROR", 46, "presentNemesisError"),
        DUPLICATE_ERROR("DUPLICATE_ERROR", 47, "presentDuplicateError"),
        CLASH_ERROR("CLASH_ERROR", 48, "sideClashError"),
        TOOL_UNBREAKABLE("TOOL_UNBREAKABLE", 49, "toolUnbreakable"),
        TOOL_DURABILITY("TOOL_DURABILITY", 50, "toolDurability"),
        MODULAR_CAPACITY("MODULAR_CAPACITY", 51, "modularCapacity"),
        COMPLEXITY_CAP("COMPLEXITY_CAP", 52, "complexityCap"),
        MAX_ENGINES("MAX_ENGINES", 53, "maxEngineCount"),
        MAX_ADDONS("MAX_ADDONS", 54, "maxAddonCount"),
        BUILDER_GROUP("BUILDER_GROUP", 55, "moduleGroupBuilder");
        private String name;

        private static final Localization.MODULE_INFO[] $VALUES = new Localization.MODULE_INFO[]{ENGINE_GROUP, DRILL_GROUP, FARMER_GROUP, CUTTER_GROUP, TANK_GROUP, ENTITY_GROUP, SHOOTER_GROUP, TOOL_GROUP, TOOL_OR_SHOOTER_GROUP, HULL_CATEGORY, ENGINE_CATEGORY, TOOL_CATEGORY, STORAGE_CATEGORY, ADDON_CATEGORY, ATTACHMENT_CATEGORY, PIG_MESSAGE, OCEAN_MESSAGE, ALPHA_MESSAGE, STORAGE_EMPTY, STORAGE_FULL, GIFT_STORAGE_FULL, EGG_STORAGE_FULL, MODULAR_COST, SIDE_NONE, SIDE_TOP, SIDE_CENTER, SIDE_BOTTOM, SIDE_BACK, SIDE_LEFT, SIDE_RIGHT, SIDE_FRONT, OCCUPIED_SIDES, AND, NO_SIDES, CONFLICT_HOWEVER, CONFLICT_ALSO, REQUIREMENT, MODULE_COUNT_1, MODULE_COUNT_2, MODULE_COUNT_3, DUPLICATES, TYPE, CAPACITY_ERROR, COMBINATION_ERROR, COMPLEXITY_ERROR, PARENT_ERROR, NEMESIS_ERROR, DUPLICATE_ERROR, CLASH_ERROR, TOOL_UNBREAKABLE, TOOL_DURABILITY, MODULAR_CAPACITY, COMPLEXITY_CAP, MAX_ENGINES, MAX_ADDONS};

        private MODULE_INFO(String var1, int var2, String name)
        {
            this.name = name;
        }

        public String translate(String ... vars)
        {
            return Localization.doTranslate("info.SC2:" + this.name, vars);
        }
    }

    public static class STORIES
    {
        public static enum THE_BEGINNING
        {
            MAP_EDITOR("MAP_EDITOR", 0, "mapEditor"),
            TITLE("TITLE", 1, "title"),
            MISSION("MISSION", 2, "mission"),
            START("START", 3, "start"),
            STOP("STOP", 4, "stop"),
            MAP("MAP", 5, "map"),
            TRACK_OPERATOR("TRACK_OPERATOR", 6, "trackOperator"),
            GOOD_JOB("GOOD_JOB", 7, "goodJob"),
            CHANGE_JUNCTIONS("CHANGE_JUNCTIONS", 8, "changeJunctions"),
            BLAST("BLAST", 9, "blast"),
            STEEL("STEEL", 10, "steel"),
            DETECTOR("DETECTOR", 11, "detector"),
            OUT_OF_REACH("OUT_OF_REACH", 12, "outOfReach"),
            OUT_OF_REACH_2("OUT_OF_REACH_2", 13, "outOfReach2"),
            LONG_JOURNEY("LONG_JOURNEY", 14, "longJourney"),
            END("END", 15, "end"),
            THANKS("THANKS", 16, "thanks"),
            LEVEL_1("LEVEL_1", 17, "level1"),
            LEVEL_2("LEVEL_2", 18, "level2"),
            LEVEL_3("LEVEL_3", 19, "level3"),
            LEVEL_4("LEVEL_4", 20, "level4"),
            LEVEL_5("LEVEL_5", 21, "level5"),
            LEVEL_6("LEVEL_6", 22, "level6"),
            LEVEL_7("LEVEL_7", 23, "level7"),
            LEVEL_8("LEVEL_8", 24, "level8"),
            LEVEL_9("LEVEL_9", 25, "level9");
            private String name;

            private static final Localization.STORIES.THE_BEGINNING[] $VALUES = new Localization.STORIES.THE_BEGINNING[]{MAP_EDITOR, TITLE, MISSION, START, STOP, MAP, TRACK_OPERATOR, GOOD_JOB, CHANGE_JUNCTIONS, BLAST, STEEL, DETECTOR, OUT_OF_REACH, OUT_OF_REACH_2, LONG_JOURNEY, END, THANKS, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5, LEVEL_6, LEVEL_7, LEVEL_8, LEVEL_9};

            private THE_BEGINNING(String var1, int var2, String name)
            {
                this.name = name;
            }

            public String translate(String ... vars)
            {
                return Localization.doTranslate("stories.beginning.SC2:" + this.name, vars);
            }
        }
    }

    public static enum UPGRADES
    {
        BLUEPRINT("BLUEPRINT", 0, "effectBlueprint"),
        COMBUSTION("COMBUSTION", 1, "effectCombustionFuel"),
        DEPLOYER("DEPLOYER", 2, "effectDeployer"),
        DISASSEMBLE("DISASSEMBLE", 3, "effectDisassemble"),
        FUEL_CAPACITY("FUEL_CAPACITY", 4, "effectFuelCapacity"),
        FUEL_COST("FUEL_COST", 5, "effectFuelCost"),
        INPUT_CHEST("INPUT_CHEST", 6, "effectInputChest"),
        BRIDGE("BRIDGE", 7, "effectManagerBridge"),
        GENERATOR("GENERATOR", 8, "effectGenerator"),
        REDSTONE("REDSTONE", 9, "effectRedstone"),
        SOLAR("SOLAR", 10, "effectSolar"),
        THERMAL("THERMAL", 11, "effectThermal"),
        FLAT("FLAT", 12, "effectTimeFlat"),
        CART_FLAT("CART_FLAT", 13, "effectTimeFlatCart"),
        FLAT_REMOVED("FLAT_REMOVED", 14, "effectTimeFlatRemove"),
        TRANSPOSER("TRANSPOSER", 15, "effectTransposer"),
        EFFICIENCY("EFFICIENCY", 16, "effectEfficiency");
        private String name;

        private static final Localization.UPGRADES[] $VALUES = new Localization.UPGRADES[]{BLUEPRINT, COMBUSTION, DEPLOYER, DISASSEMBLE, FUEL_CAPACITY, FUEL_COST, INPUT_CHEST, BRIDGE, GENERATOR, REDSTONE, SOLAR, THERMAL, FLAT, CART_FLAT, FLAT_REMOVED, TRANSPOSER, EFFICIENCY};

        private UPGRADES(String var1, int var2, String name)
        {
            this.name = name;
        }

        public String translate(String ... vars)
        {
            return Localization.doTranslate("info.SC2:" + this.name, vars);
        }
    }
}
