package com.namenull.extrautilwateringcantweak;

import com.rwtema.extrautils.item.ItemWateringCan;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.crop.TileEntityCrop;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;


@Mod(modid = "WateringCanMod", version = "1.1", dependencies = "required-after:ExtraUtilities;required-after:IC2", guiFactory = "com.namenull.extrautilwateringcantweak.WateringCanMod$ConfigGuiFactory")
public class WateringCanMod {
    // logger
	public static final Logger LOGGER = LogManager.getLogger("WateringCanMod");
    
	// watering can base-type
    public static ItemWateringCanIC2 wateringCanIC2;
    
    // configurations
    public static Configuration config;
    public static float ic2TickChance;
    public static int ic2TickMultiplier;
    
    // basic_watering_can Configs
    public static boolean basicWaterCanStatChange;
    public static boolean basicWaterCanNutrientChange;
    
    public static float basicWaterStatChangeChance_Growth;
    public static int basicWaterStatChangeValue_Growth;
    public static int basicWaterStatChangeMax_Growth;
    public static int basicWaterStatChangeMin_Growth;
    
    public static float basicWaterStatChangeChance_Gain;
    public static int basicWaterStatChangeValue_Gain;
    public static int basicWaterStatChangeMax_Gain;
    public static int basicWaterStatChangeMin_Gain;
    
    public static float basicWaterStatChangeChance_Resist;
    public static int basicWaterStatChangeValue_Resist;
    public static int basicWaterStatChangeMax_Resist;
    public static int basicWaterStatChangeMin_Resist;
    
    public static int basicWaterStatChangeValue_Nutrients;
    public static int basicWaterStatChangeMax_Nutrients;
    
    public static int basicWaterStatChangeValue_Hydration;
    public static int basicWaterStatChangeMax_Hydration;
    
    public static int basicWaterStatChangeValue_WeedEx;
    public static int basicWaterStatChangeMax_WeedEx;

    // reinforced_watering_can Configs
    public static boolean reinforcedWaterCanStatChange;
    public static boolean reinforcedWaterCanNutrientChange;
    
    public static float reinforcedWaterStatChangeChance_Growth;
    public static int reinforcedWaterStatChangeValue_Growth;
    public static int reinforcedWaterStatChangeMax_Growth;
    public static int reinforcedWaterStatChangeMin_Growth;
    
    public static float reinforcedWaterStatChangeChance_Gain;
    public static int reinforcedWaterStatChangeValue_Gain;
    public static int reinforcedWaterStatChangeMax_Gain;
    public static int reinforcedWaterStatChangeMin_Gain;
    
    public static float reinforcedWaterStatChangeChance_Resist;
    public static int reinforcedWaterStatChangeValue_Resist;
    public static int reinforcedWaterStatChangeMax_Resist;
    public static int reinforcedWaterStatChangeMin_Resist;
    
    public static int reinforcedWaterStatChangeValue_Nutrients;
    public static int reinforcedWaterStatChangeMax_Nutrients;
    
    public static int reinforcedWaterStatChangeValue_Hydration;
    public static int reinforcedWaterStatChangeMax_Hydration;
    
    public static int reinforcedWaterStatChangeValue_WeedEx;
    public static int reinforcedWaterStatChangeMax_WeedEx;
    
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	config = new Configuration(new java.io.File("config/WateringCanMod.cfg"));
        try {
            config.load();
            loadConfig();
        } catch (Exception e) {
            LOGGER.error("Error loading config: {}", e.getMessage());
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    	
        wateringCanIC2 = new ItemWateringCanIC2();
        GameRegistry.registerItem(wateringCanIC2, "watering_can_ic2");
        GameRegistry.addShapelessRecipe(new ItemStack(wateringCanIC2, 1, 0), new ItemStack(com.rwtema.extrautils.ExtraUtils.wateringCan, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(wateringCanIC2, 1, 3), new ItemStack(com.rwtema.extrautils.ExtraUtils.wateringCan, 1, 3));
        GameRegistry.addShapelessRecipe(new ItemStack(com.rwtema.extrautils.ExtraUtils.wateringCan, 1, 0), new ItemStack(wateringCanIC2, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(com.rwtema.extrautils.ExtraUtils.wateringCan, 1, 3), new ItemStack(wateringCanIC2, 1, 3));
        
        LOGGER.info("Watering Can Mod Initialized!");
    }

    private static void loadConfig() {
    	ic2TickChance = config.getFloat("ic2TickChance", "General", 0.30F, 0.0001F, 1.0F, "Percentage chance (0.0001 to 1.0) to tick an IC2 crop per game tick.");
        ic2TickMultiplier = config.getInt("ic2TickMultiplier", "General", 1, 0, 10, "Number of times (0 to 10) to call tick() when the random check succeeds.");
        
        // basic_watering_can Configs
        basicWaterCanStatChange = config.getBoolean("basicWaterCanAllowStatChange", "General", true,
            "If stat changes are applied whenever the basic_watering_can tries to tick a crop.");
        basicWaterCanNutrientChange = config.getBoolean("basicWaterCanAllowNutrientChange", "General", true,
        	"If nutrient changes are applied whenever the basic_watering_can tries to tick a crop.");
        
        // Growth
        basicWaterStatChangeChance_Growth = config.getFloat("basicWaterCanChanceGrowth", "basic_watering_can", 0.025F, 0.0F, 1.0F,
            "Percentage chance (0.0 to 1.0) whenever a tick is applied to update the IC2 Crop Growth Stat.");
        basicWaterStatChangeValue_Growth = config.getInt("basicWaterCanValueGrowth", "basic_watering_can", 1, -1, 1,
            "The value (-1 to 1) to change the Growth Stat by.");
        basicWaterStatChangeMax_Growth = config.getInt("basicWaterCanMaxGrowth", "basic_watering_can", 5, 0, 31,
            "The maximum value the Watering Can will increase the Growth stat to, if incrementing.");
        basicWaterStatChangeMin_Growth = config.getInt("basicWaterCanMinGrowth", "basic_watering_can", 0, 0, 31,
            "The minimum value the Watering Can will decrease the Growth stat to, if decrementing.");

        // Gain
        basicWaterStatChangeChance_Gain = config.getFloat("basicWaterCanChanceGain", "basic_watering_can", 0.025F, 0.0F, 1.0F,
            "Percentage chance (0.0 to 1.0) when a tick is applied to update the IC2 Crop Gain Stat.");
        basicWaterStatChangeValue_Gain = config.getInt("basicWaterCanValueGain", "basic_watering_can", 1, -1, 1,
            "The value (-1 to 1) to change the Gain Stat by.");
        basicWaterStatChangeMax_Gain = config.getInt("basicWaterCanMaxGain", "basic_watering_can", 5, 0, 31,
            "The maximum value the Watering Can will increase the Gain stat to, if incrementing.");
        basicWaterStatChangeMin_Gain = config.getInt("basicWaterCanMinGain", "basic_watering_can", 0, 0, 31,
            "The minimum value the Watering Can will decrease the Gain stat to, if decrementing.");

        // Resistance
        basicWaterStatChangeChance_Resist = config.getFloat("basicWaterCanChanceResist", "basic_watering_can", 0.025F, 0.0F, 1.0F,
            "Percentage chance (0.0 to 1.0) when a tick is applied to update the IC2 Crop Resistance Stat.");
        basicWaterStatChangeValue_Resist = config.getInt("basicWaterCanValueResist", "basic_watering_can", 0, -1, 1,
            "The value (-1 to 1) to change the Resistance Stat by.");
        basicWaterStatChangeMax_Resist = config.getInt("basicWaterCanMaxResist", "basic_watering_can", 5, 0, 31,
            "The maximum value the Watering Can will increase the Resistance stat to, if incrementing.");
        basicWaterStatChangeMin_Resist = config.getInt("basicWaterCanMinResist", "basic_watering_can", 0, 0, 31,
            "The minimum value the Watering Can will decrease the Resistance stat to, if decrementing.");

        // Nutrients
        basicWaterStatChangeValue_Nutrients = config.getInt("basicWaterCanValueNutrients", "basic_watering_can", 2, 0, 100,
            "The value to add to the Nutrients storage (0 to 100).");
        basicWaterStatChangeMax_Nutrients = config.getInt("basicWaterCanMaxNutrients", "basic_watering_can", 5, 0, 100,
            "The maximum value the Watering Can will increase the Nutrients storage to.");

        // Hydration
        basicWaterStatChangeValue_Hydration = config.getInt("basicWaterCanValueHydration", "basic_watering_can", 2, 0, 100,
            "The value to add to the Hydration storage (0 to 100).");
        basicWaterStatChangeMax_Hydration = config.getInt("basicWaterCanMaxHydration", "basic_watering_can", 5, 0, 100,
            "The maximum value the Watering Can will increase the Hydration storage to.");
        
        // Weed-EX
        basicWaterStatChangeValue_WeedEx = config.getInt("basicWaterCanValueWeedEx", "basic_watering_can", 2, 0, 100,
            "The value to add to the Weed-EX storage (0 to 75 to avoid stat loss).");
        basicWaterStatChangeMax_WeedEx = config.getInt("basicWaterCanMaxWeedEx", "basic_watering_can", 0, 0, 100,
            "The maximum value the Watering Can will increase the Weed-EX storage to (≤75 recommended).");
        
        
        // reinforced_watering_can Configs
        reinforcedWaterCanStatChange = config.getBoolean("reinforcedWaterCanAllowStatChange", "General", true,
            "If stat changes are applied whenever the reinforced_watering_can tries to tick a crop.");
        reinforcedWaterCanNutrientChange = config.getBoolean("reinforcedWaterCanAllowNutrientChange", "General", true,
            "If nutrient changes are applied whenever the reinforced_watering_can tries to tick a crop.");
        
        // Growth
        reinforcedWaterStatChangeChance_Growth = config.getFloat("reinforcedWaterCanChanceGrowth", "reinforced_watering_can", 0.05F, 0.0F, 1.0F,
            "Percentage chance (0.0 to 1.0) when a tick is applied to update the IC2 Crop Growth Stat.");
        reinforcedWaterStatChangeValue_Growth = config.getInt("reinforcedWaterCanValueGrowth", "reinforced_watering_can", 1, -1, 1,
            "The value (-1 to 1) to change the Growth Stat by.");
        reinforcedWaterStatChangeMax_Growth = config.getInt("reinforcedWaterCanMaxGrowth", "reinforced_watering_can", 15, 0, 31,
            "The maximum value the Watering Can will increase the Growth stat to, if incrementing.");
        reinforcedWaterStatChangeMin_Growth = config.getInt("reinforcedWaterCanMinGrowth", "reinforced_watering_can", 0, 0, 31,
            "The minimum value the Watering Can will decrease the Growth stat to, if decrementing.");

        // Gain
        reinforcedWaterStatChangeChance_Gain = config.getFloat("reinforcedWaterCanChanceGain", "reinforced_watering_can", 0.05F, 0.0F, 1.0F,
            "Percentage chance (0.0 to 1.0) when a tick is applied to update the IC2 Crop Gain Stat.");
        reinforcedWaterStatChangeValue_Gain = config.getInt("reinforcedWaterCanValueGain", "reinforced_watering_can", 1, -1, 1,
            "The value (-1 to 1) to change the Gain Stat by.");
        reinforcedWaterStatChangeMax_Gain = config.getInt("reinforcedWaterCanMaxGain", "reinforced_watering_can", 15, 0, 31,
            "The maximum value the Watering Can will increase the Gain stat to, if incrementing.");
        reinforcedWaterStatChangeMin_Gain = config.getInt("reinforcedWaterCanMinGain", "reinforced_watering_can", 0, 0, 31,
            "The minimum value the Watering Can will decrease the Gain stat to, if decrementing.");

        // Resistance
        reinforcedWaterStatChangeChance_Resist = config.getFloat("reinforcedWaterCanChanceResist", "reinforced_watering_can", 0.05F, 0.0F, 1.0F,
            "Percentage chance (0.0 to 1.0) when a tick is applied to update the IC2 Crop Resistance Stat.");
        reinforcedWaterStatChangeValue_Resist = config.getInt("reinforcedWaterCanValueResist", "reinforced_watering_can", -1, -1, 1,
            "The value (-1 to 1) to change the Resistance Stat by.");
        reinforcedWaterStatChangeMax_Resist = config.getInt("reinforcedWaterCanMaxResist", "reinforced_watering_can", 10, 0, 31,
            "The maximum value the Watering Can will increase the Resistance stat to, if incrementing.");
        reinforcedWaterStatChangeMin_Resist = config.getInt("reinforcedWaterCanMinResist", "reinforced_watering_can", 0, 0, 31,
            "The minimum value the Watering Can will decrease the Resistance stat to, if decrementing.");

        // Nutrients
        reinforcedWaterStatChangeValue_Nutrients = config.getInt("reinforcedWaterCanValueNutrients", "reinforced_watering_can", 5, 0, 100,
            "The value to add to the Nutrients storage (0 to 100).");
        reinforcedWaterStatChangeMax_Nutrients = config.getInt("reinforcedWaterCanMaxNutrients", "reinforced_watering_can", 50, 0, 100,
            "The maximum value the Watering Can will increase the Nutrients storage to.");

        // Hydration
        reinforcedWaterStatChangeValue_Hydration = config.getInt("reinforcedWaterCanValueHydration", "reinforced_watering_can", 5, 0, 100,
            "The value to add to the Hydration storage (0 to 100).");
        reinforcedWaterStatChangeMax_Hydration = config.getInt("reinforcedWaterCanMaxHydration", "reinforced_watering_can", 75, 0, 100,
            "The maximum value the Watering Can will increase the Hydration storage to.");

        // Weed-EX
        reinforcedWaterStatChangeValue_WeedEx = config.getInt("reinforcedWaterCanValueWeedEx", "reinforced_watering_can", 5, 0, 100,
            "The value to add to the Weed-EX storage (0 to 75 to avoid stat loss).");
        reinforcedWaterStatChangeMax_WeedEx = config.getInt("reinforcedWaterCanMaxWeedEx", "reinforced_watering_can", 25, 0, 100,
            "The maximum value the Watering Can will increase the Weed-EX storage to (≤75 recommended).");
    }
    
 // GUI Factory for Mod Options
    public static class ConfigGuiFactory implements cpw.mods.fml.client.IModGuiFactory {
        @Override
        public void initialize(Minecraft minecraftInstance) {
            // No initialization needed
        }

        @Override
        public Class<? extends GuiScreen> mainConfigGuiClass() {
            return ConfigGui.class;
        }

        @Override
        public java.util.Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
            return null; // No runtime categories
        }

        @Override
        public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
            return null; // No custom handlers
        }
    }
    
 // Config GUI
    @SideOnly(Side.CLIENT)
    public static class ConfigGui extends GuiConfig {
        public ConfigGui(GuiScreen parent) {
            super(
                parent,
                getConfigElements(),
                "WateringCanMod",
                false, // No world restart required
                false, // No server restart required
                "Watering Can Mod Configuration"
            );
        }

        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> list = new ArrayList<IConfigElement>();
            list.add(new ConfigElement(config.getCategory("general")));
            list.add(new ConfigElement(config.getCategory("basic_watering_can")));
            list.add(new ConfigElement(config.getCategory("reinforced_watering_can")));
            return list;
        }
        
        @Override
        protected void actionPerformed(GuiButton button) {
            super.actionPerformed(button);
            if (button.id == 2000) { // ID 2000 is the "Done" button
                LOGGER.info("Config GUI closed, saving and reloading config...");
                if (config.hasChanged()) {
                    config.save();
                    loadConfig(); // Reload values after saving
                }
            }
        }
    }
    
    public static class ItemWateringCanIC2 extends ItemWateringCan {
        @Override
        public void waterLocation(World worldObj, double hitX, double hitY, double hitZ, int side, ItemStack stack, EntityPlayer play) {
            // Get the basic or reinforced range.
        	int range = stack.getItemDamage() == 3 ? 3 : 1;

            // Apply the splash water effect
            if (worldObj.isRemote) {
                double dx = Facing.offsetsXForSide[side];
                double dy = Facing.offsetsYForSide[side];
                double dz = Facing.offsetsZForSide[side];
                double x2 = hitX + dx * 0.1;
                double y2 = hitY + dy * 0.1;
                double z2 = hitZ + dz * 0.1;

                int particleCount = stack.getItemDamage() == 2 ? 1 :
                                   stack.getItemDamage() == 3 ? 100 : 10;
                for (int i = 0; i < particleCount; i++) {
                    worldObj.spawnParticle("splash",
                        x2 + worldObj.rand.nextGaussian() * 0.6 * range,
                        y2,
                        z2 + worldObj.rand.nextGaussian() * 0.6 * range,
                        0.0, 0.0, 0.0);
                }
                return;
            }

            // get the cords of the target block
            int blockX = (int) Math.floor(hitX);
            int blockY = (int) Math.floor(hitY);
            int blockZ = (int) Math.floor(hitZ);

            // iterate around the target block using the range
            for (int x = blockX - range; x <= blockX + range; x++) {
                for (int y = blockY - range; y <= blockY + range; y++) {
                    for (int z = blockZ - range; z <= blockZ + range; z++) {
                        if (worldObj.isAirBlock(x, y, z)) {
                            continue;
                        }

                        Block id = worldObj.getBlock(x, y, z);

                        // if target block is farmland and is not wet, makes it wet.
                        if (id == Blocks.farmland && worldObj.getBlockMetadata(x, y, z) < 7) {
                            worldObj.setBlockMetadataWithNotify(x, y, z, 7, 2);
                        }

                        // This is how the extra utilities watering can applies random ticks to plants
                        int timer = -1;
                        if (id == Blocks.wheat || id instanceof IPlantable || id instanceof IGrowable) {
                            timer = 40;
                        } else if (id instanceof BlockSapling) {
                            timer = 50;
                        }

                        if (stack.getItemDamage() == 2) {
                            timer *= 20;
                        }
                        if (timer > 0) {
                            timer /= range;
                        }

                        if (timer > 0 && id.getTickRandomly()) {
                            worldObj.scheduleBlockUpdate(x, y, z, id, worldObj.rand.nextInt(timer));
                        }

                        // this is the new logic for handling IC2 crops
                        TileEntity tile = worldObj.getTileEntity(x, y, z);
                        if (tile instanceof TileEntityCrop) {
                            TileEntityCrop crop = (TileEntityCrop) tile;
                            if (crop.canUpdate() && !crop.isInvalid() && worldObj.rand.nextFloat() <= ic2TickChance) {
                            	boolean isReinforced = stack.getItemDamage() == 3;
                            	boolean allowStatChange = isReinforced ? reinforcedWaterCanStatChange : basicWaterCanStatChange;
                            	boolean allowNutrientChange = isReinforced ? reinforcedWaterCanNutrientChange : basicWaterCanNutrientChange;
                            	
                            	if (allowStatChange) {
                            	
                            		// Growth
                                    float chance = isReinforced ? reinforcedWaterStatChangeChance_Growth : basicWaterStatChangeChance_Growth;
                                    int value = isReinforced ? reinforcedWaterStatChangeValue_Growth : basicWaterStatChangeValue_Growth;
                                    int max = isReinforced ? reinforcedWaterStatChangeMax_Growth : basicWaterStatChangeMax_Growth;
                                    int min = isReinforced ? reinforcedWaterStatChangeMin_Growth : basicWaterStatChangeMin_Growth;
                                    int currValue = crop.getGrowth();
                                    
                                    if (!((currValue >= max && value > 0) || (currValue <= min && value < 0))) {
                                    	float ttvalue = worldObj.rand.nextFloat();
                                        if (ttvalue <= chance) {
                                            if (currValue + value >= max) {
                                                crop.setGrowth((byte) max);
                                            } else if (currValue + value <= min) {
                                                crop.setGrowth((byte) min);
                                            } else {
                                                crop.setGrowth((byte) (currValue + value));
                                            }
                                        }
                                    }
                                    
                                    // Gain
                                    chance = isReinforced ? reinforcedWaterStatChangeChance_Gain : basicWaterStatChangeChance_Gain;
                                    value = isReinforced ? reinforcedWaterStatChangeValue_Gain : basicWaterStatChangeValue_Gain;
                                    max = isReinforced ? reinforcedWaterStatChangeMax_Gain : basicWaterStatChangeMax_Gain;
                                    min = isReinforced ? reinforcedWaterStatChangeMin_Gain : basicWaterStatChangeMin_Gain;
                                    currValue = crop.getGain();
                                    
                                    if (!((currValue >= max && value > 0) || (currValue <= min && value < 0))) {
                                        if (worldObj.rand.nextFloat() < chance) {
                                            if (currValue + value >= max) {
                                                crop.setGain((byte) max);
                                            } else if (currValue + value <= min) {
                                                crop.setGain((byte) min);
                                            } else {
                                                crop.setGain((byte) (currValue + value));
                                            }
                                        }
                                    }
                                    
                                    // Resistance
                                    chance = isReinforced ? reinforcedWaterStatChangeChance_Resist : basicWaterStatChangeChance_Resist;
                                    value = isReinforced ? reinforcedWaterStatChangeValue_Resist : basicWaterStatChangeValue_Resist;
                                    max = isReinforced ? reinforcedWaterStatChangeMax_Resist : basicWaterStatChangeMax_Resist;
                                    min = isReinforced ? reinforcedWaterStatChangeMin_Resist : basicWaterStatChangeMin_Resist;
                                    currValue = crop.getResistance();
                                    
                                    if (!((currValue >= max && value > 0) || (currValue <= min && value < 0))) {
                                        if (worldObj.rand.nextFloat() < chance) {
                                            if (currValue + value >= max) {
                                                crop.setResistance((byte) max);
                                            } else if (currValue + value <= min) {
                                                crop.setResistance((byte) min);
                                            } else {
                                                crop.setResistance((byte) (currValue + value));
                                            }
                                        }
                                    }
                            		
                            		
                            	}
                            	
                            	
                            	if (allowNutrientChange) {
                                    // Nutrients
                                    int changeValue = isReinforced ? reinforcedWaterStatChangeValue_Nutrients : basicWaterStatChangeValue_Nutrients;
                                    int maxValue = isReinforced ? reinforcedWaterStatChangeMax_Nutrients : basicWaterStatChangeMax_Nutrients;
                                    
                                    int currValue = crop.getNutrientStorage();
                                    if (currValue < maxValue) {
                                        if (currValue + changeValue > maxValue) {
                                            crop.setNutrientStorage(maxValue);
                                        } else {
                                            crop.setNutrientStorage(currValue + changeValue);
                                        }
                                    }
                                    
                                    // Hydration
                                    changeValue = isReinforced ? reinforcedWaterStatChangeValue_Hydration : basicWaterStatChangeValue_Hydration;
                                    maxValue = isReinforced ? reinforcedWaterStatChangeMax_Hydration : basicWaterStatChangeMax_Hydration;
                                    
                                    currValue = crop.getHydrationStorage();
                                    if (currValue < maxValue) {
                                        if (currValue + changeValue > maxValue) {
                                            crop.setHydrationStorage(maxValue);
                                        } else {
                                            crop.setHydrationStorage(currValue + changeValue);
                                        }
                                    }
                                    
                                    // WeedEx
                                    changeValue = isReinforced ? reinforcedWaterStatChangeValue_WeedEx : basicWaterStatChangeValue_WeedEx;
                                    maxValue = isReinforced ? reinforcedWaterStatChangeMax_WeedEx : basicWaterStatChangeMax_WeedEx;
                                    
                                    currValue = crop.getWeedExStorage();
                                    if (currValue < maxValue) {
                                        if (currValue + changeValue > maxValue) {
                                            crop.setWeedExStorage(maxValue);
                                        } else {
                                            crop.setWeedExStorage(currValue + changeValue);
                                        }
                                    }
                                }
                            	
                            	
                            	
                                for (int i = 0; i < ic2TickMultiplier; i++) {
                                    crop.tick();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}