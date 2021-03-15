package github.BTEPlotSystem.core.menus;

import github.BTEPlotSystem.core.plots.Plot;
import github.BTEPlotSystem.core.plots.PlotHandler;
import github.BTEPlotSystem.core.plots.PlotManager;
import github.BTEPlotSystem.utils.*;
import github.BTEPlotSystem.utils.enums.Status;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewMenu implements Listener {
    private Inventory reviewMenu;
    private Inventory reviewPlotMenu;

    //Review Inventory
    private ItemStack itemArrowLeft;
    private ItemStack itemArrowRight;
    private ItemStack itemClose;

    //Review Plot Inventory
    private ItemStack itemMap;
    private ItemStack itemCancel;
    private ItemStack itemSubmit;

    private final ItemStack[] itemCategory = new ItemStack[4];
    private final ItemStack[] itemPointZero = new ItemStack[4];
    private final ItemStack[] itemPointOne = new ItemStack[4];
    private final ItemStack[] itemPointTwo = new ItemStack[4];
    private final ItemStack[] itemPointThree = new ItemStack[4];
    private final ItemStack[] itemPointFour = new ItemStack[4];
    private final ItemStack[] itemPointFive = new ItemStack[4];

    private final HashMap<Plot, ItemStack> plotItems = new HashMap<>();
    private Plot selectedPlot;

    private final Player player;

    public ReviewMenu(Player player) throws SQLException {
        //Opens Review Menu, showing all plots in the given round.
        reviewMenu = Bukkit.createInventory(player, 54, "Review & Manage Plots");
        this.player = player;

        ItemStack plotLoadError = new ItemBuilder(Material.BARRIER, 1)
                .setName("§cCould not load plot")
                .setLore(new LoreBuilder()
                        .description("Please contact a Manager or Developer!")
                        .build())
                .build();

        List<Plot> plots = PlotManager.getPlots(Status.unreviewed);
        plots.addAll(PlotManager.getPlots(Status.unfinished));

        int counter = 0;
        for (Plot plot : plots) {
            try {
                switch (plot.getStatus()) {
                    case unfinished:
                        plotItems.put(plot, new ItemBuilder(Material.WOOL, 1, (byte) 1)
                                .setName("§b§lManage Plot")
                                .setLore(new LoreBuilder()
                                        .description(
                                                "§7ID: §f" + plot.getID(),
                                                "",
                                                "§7Builder: §f" + plot.getBuilder().getName(),
                                                "§7City: §f" + plot.getCity().getName(),
                                                "§7Difficulty: §f" + plot.getDifficulty().name().charAt(0) + plot.getDifficulty().name().substring(1).toLowerCase())
                                        .build())
                                .build());
                        break;
                    case unreviewed:
                        plotItems.put(plot, new ItemBuilder(Material.MAP, 1)
                                .setName("§b§lReview Plot")
                                .setLore(new LoreBuilder()
                                        .description(
                                                "§7ID: §f" + plot.getID(),
                                                "",
                                                "§7Builder: §f" + plot.getBuilder().getName(),
                                                "§7City: §f" + plot.getCity().getName(),
                                                "§7Difficulty: §f" + plot.getDifficulty().name().charAt(0) + plot.getDifficulty().name().substring(1).toLowerCase())
                                        .build())
                                .build());
                        break;
                    default:
                        reviewMenu.setItem(counter, plotLoadError);
                        return;
                }
                reviewMenu.setItem(counter, plotItems.get(plot));
            } catch (Exception e) {
                reviewMenu.setItem(counter, plotLoadError);
                e.printStackTrace();
            }
            counter++;
        }

        for (int i = 45; i < 54; i++) {
            switch (i) {
                case 46:
                    itemArrowLeft = new ItemBuilder(Material.ARROW, 1)
                            .setName("§6§lPrevious Page")
                            .setLore(new LoreBuilder()
                                    .description("§7Show the previous page")
                                    .build())
                            .build();
                    reviewMenu.setItem(i, itemArrowLeft);
                    break;
                case 49:
                    itemClose = new ItemBuilder(Material.BARRIER, 1)
                            .setName("§c§lCLOSE")
                            .setLore(new LoreBuilder()
                                    .description("§7Close the menu")
                                    .build())
                            .build();
                    reviewMenu.setItem(i, itemClose);
                    break;
                case 52:
                    itemArrowRight = new ItemBuilder(Material.ARROW, 1)
                            .setName("§6§lNext Page")
                            .setLore(new LoreBuilder()
                                    .description("§7Show the next page")
                                    .build())
                            .build();
                    reviewMenu.setItem(i, itemArrowRight);
                    break;
                default:
                    reviewMenu.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName(" ").build());
            }
        }
        player.openInventory(reviewMenu);
    }

    public ReviewMenu(Player player, Plot plot) {
        this.player = player;
        selectedPlot = plot;

        ReviewPlot(plot);
    }

    private void ReviewPlot(Plot plot) {
        reviewPlotMenu = Bukkit.createInventory(player, 54, "Review Plot #" + plot.getID());

        for (int i = 0; i < 54; i++) {
            reviewPlotMenu.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName(" ").build());

            switch (i) {
                case 4:
                    itemMap = new ItemBuilder(Material.MAP, 1)
                            .setName("§b§lReview Plot")
                            .setLore(new LoreBuilder()
                                    .description(
                                            "§7ID: §f" + plot.getID(),
                                            "",
                                            "§7Builder: §f" + plot.getBuilder().getName(),
                                            "§7City: §f" + plot.getCity().getName(),
                                            "§7Difficulty: §f" + plot.getDifficulty().name().charAt(0) + plot.getDifficulty().name().substring(1).toLowerCase())
                                    .build())
                            .build();
                    reviewPlotMenu.setItem(i, itemMap);
                    break;
                case 10:
                    itemCategory[0] = new ItemBuilder(Material.ARROW, 1)
                            .setName("§a§lACCURACY")
                            .setLore(new LoreBuilder()
                                    .description("§7How accurate is the building?")
                                    .build())
                            .build();
                    reviewPlotMenu.setItem(i, itemCategory[0]);
                    break;
                case 19:
                    itemCategory[1] = new ItemBuilder(Material.PAINTING, 1)
                            .setName("§a§lBLOCK PALETTE")
                            .setLore(new LoreBuilder()
                                    .description("§7How many different blocks are used and how creative are they?")
                                    .build())
                            .build();
                    reviewPlotMenu.setItem(i, itemCategory[1]);
                    break;
                case 28:
                    itemCategory[2] = new ItemBuilder(Material.EYE_OF_ENDER, 1)
                            .setName("§a§lDETAILING")
                            .setLore(new LoreBuilder()
                                    .description("§7How much detail does the building have?")
                                    .build())
                            .build();
                    reviewPlotMenu.setItem(i, itemCategory[2]);
                    break;
                case 37:
                    itemCategory[3] = new ItemBuilder(Material.WOOD_AXE, 1)
                            .setName("§a§lTECHNIQUE")
                            .setLore(new LoreBuilder()
                                    .description("§7What building techniques have been used and how creative are they?")
                                    .build())
                            .build();
                    reviewPlotMenu.setItem(i, itemCategory[3]);
                    break;
                case 48:
                    itemSubmit = new ItemBuilder(Material.CONCRETE, 1, (byte) 13)
                            .setName("§a§lSUBMIT")
                            .setLore(new LoreBuilder()
                                    .description("§7Submit selected points and mark plot as reviewed")
                                    .build())
                            .build();
                    reviewPlotMenu.setItem(i, itemSubmit);
                    break;
                case 50:

                    itemCancel = new ItemBuilder(Material.CONCRETE, 1, (byte) 14)
                            .setName("§c§lCANCEL")
                            .setLore(new LoreBuilder()
                                    .description("§7Close the menu")
                                    .build())
                            .build();
                    reviewPlotMenu.setItem(i, itemCancel);
                    break;
                default:
                    int column = (i % 9) + 1;
                    int row = (i - (i % 9)) / 9 + 1;
                    if (column > 2 && column < 9 && row > 1 && row < 6) {
                        if ((i + 1) % 9 == 3) {
                            itemPointZero[((i + 1) - (i + 1) % 9) / 54] = new ItemBuilder(Material.WOOL, 1, (byte) 8)
                                    .setName("§l§70 Points")
                                    .setLore(new LoreBuilder()
                                            .description("§7Click to select")
                                            .build())
                                    .build();

                            //Add Enchantment
                            ItemMeta itemMeta = itemPointZero[((i + 1) - (i + 1) % 9) / 54].getItemMeta();
                            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                            itemPointZero[((i + 1) - (i + 1) % 9) / 54].setItemMeta(itemMeta);
                            reviewPlotMenu.setItem(i, itemPointZero[(i - (i + 1) % 9) / 54]);
                        } else if ((i + 1) % 9 == 4) {
                            itemPointOne[((i + 1) - (i + 1) % 9) / 54] = new ItemBuilder(Material.WOOL, 1, (byte) 14)
                                    .setName("§l§c1 Point")
                                    .setLore(new LoreBuilder()
                                            .description("§7Click to select")
                                            .build())
                                    .build();

                            reviewPlotMenu.setItem(i, itemPointOne[(i - (i + 1) % 9) / 54]);
                        } else if ((i + 1) % 9 == 5) {
                            itemPointTwo[((i + 1) - (i + 1) % 9) / 54] = new ItemBuilder(Material.WOOL, 2, (byte) 1)
                                    .setName("§l§62 Points")
                                    .setLore(new LoreBuilder()
                                            .description("§7Click to select")
                                            .build())
                                    .build();
                            reviewPlotMenu.setItem(i, itemPointTwo[(i - (i + 1) % 9) / 54]);
                        } else if ((i + 1) % 9 == 6) {
                            itemPointThree[((i + 1) - (i + 1) % 9) / 54] = new ItemBuilder(Material.WOOL, 3, (byte) 4)
                                    .setName("§l§e3 Points")
                                    .setLore(new LoreBuilder()
                                            .description("§7Click to select")
                                            .build())
                                    .build();
                            reviewPlotMenu.setItem(i, itemPointThree[(i - (i + 1) % 9) / 54]);
                        } else if ((i + 1) % 9 == 7) {
                            itemPointFour[((i + 1) - (i + 1) % 9) / 54] = new ItemBuilder(Material.WOOL, 4, (byte) 13)
                                    .setName("§l§24 Points")
                                    .setLore(new LoreBuilder()
                                            .description("§7Click to select")
                                            .build())
                                    .build();
                            reviewPlotMenu.setItem(i, itemPointFour[(i - (i + 1) % 9) / 54]);
                        } else if ((i + 1) % 9 == 8) {
                            itemPointFive[((i + 1) - (i + 1) % 9) / 54] = new ItemBuilder(Material.WOOL, 5, (byte) 5)
                                    .setName("§l§a5 Points")
                                    .setLore(new LoreBuilder()
                                            .description("§7Click to select")
                                            .build())
                                    .build();
                            reviewPlotMenu.setItem(i, itemPointFive[(i - (i + 1) % 9) / 54]);
                        }
                    }
                    break;
            }
        }
        player.openInventory(reviewPlotMenu);
    }

    @EventHandler
    public void onPlayerInventoryClickEvent(InventoryClickEvent event) {
        try {
            if (event.getWhoClicked().equals(player)) {
                if (event.getClickedInventory().equals(reviewMenu)) {

                    if (event.getCurrentItem().equals(itemClose)) {
                        event.getWhoClicked().closeInventory();

                    } else if (event.getCurrentItem().equals(itemArrowLeft)) {

                    } else if (event.getCurrentItem().equals(itemArrowRight)) {
                    }

                    for (ItemStack plotItem : plotItems.values()) {
                        if (event.getCurrentItem().equals(plotItem)) {
                            selectedPlot = getPlotByValue(plotItem);
                            if (selectedPlot.getStatus() == Status.unreviewed) {
                                event.getWhoClicked().closeInventory();
                                if (!selectedPlot.getBuilder().getUUID().toString().equals(event.getWhoClicked().getUniqueId().toString())){
                                    PlotHandler.teleportPlayer(selectedPlot, player);
                                } else {
                                    event.getWhoClicked().sendMessage(Utils.getErrorMessageFormat("You cannot review your own builds!"));
                                }
                            } else {
                                event.getWhoClicked().closeInventory();
                                new PlotActionsMenu(selectedPlot, player);
                            }
                        }
                    }
                    event.setCancelled(true);
                } else if (event.getClickedInventory().equals(reviewPlotMenu)) {
                    if (event.getCurrentItem().equals(itemCancel)) {
                        event.getWhoClicked().closeInventory();
                    } else if (event.getCurrentItem().equals(itemSubmit)) {
                        StringBuilder score = new StringBuilder();

                        int totalRating = 0;
                        boolean isRejected = false;

                        for (int i = 0; i < 4; i++) {
                            for (int j = 0; j < 6; j++) {
                                if (reviewPlotMenu.getItem(11 + (i * 9) + j).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE)) {
                                    switch (i) {
                                        case 0:
                                            player.sendMessage("§7>> Accuracy: " + j);
                                            score.append(j).append(",");
                                            break;
                                        case 1:
                                            player.sendMessage("§7>> Block Palette: " + j);
                                            score.append(j).append(",");
                                            break;
                                        case 2:
                                            player.sendMessage("§7>> Detailing: " + j);
                                            score.append(j).append(",");
                                            break;
                                        case 3:
                                            player.sendMessage("§7>> Technique: " + j);
                                            score.append(j);
                                            break;
                                    }
                                    totalRating+=j;
                                    if (j <= 0){ isRejected = true; }
                                }
                            }
                        }
                        if (totalRating <= 8){ isRejected = true; }

                        //TODO: multiply with multiplier
                        selectedPlot.setScore(totalRating);

                        if (!isRejected){
                            player.sendMessage("§7>> §aPlot #" + selectedPlot.getID() + " by " + selectedPlot.getBuilder().getName() + " marked as reviewed!");

                            selectedPlot.setStatus(Status.complete);
                            selectedPlot.getBuilder().addScore(totalRating);
                            selectedPlot.getBuilder().addCompletedBuild();
                            selectedPlot.getBuilder().removePlot(selectedPlot.getSlot());
                        } else {
                            player.sendMessage("§7>> §aPlot #" + selectedPlot.getID() + " by " + selectedPlot.getBuilder().getName() + " has been rejected! Send feedback using §6/sendFeedback <ID> <Text> §a!");

                            PlotHandler.undoSubmit(selectedPlot);
                        }

                        if(selectedPlot.isReviewed()) {
                            selectedPlot.getReview().setRating(score.toString());
                            selectedPlot.getReview().setReviewer(player.getUniqueId());
                        } else {
                            new Review(selectedPlot.getID(), player.getUniqueId(), score.toString());
                        }

                        for(Player player : player.getWorld().getPlayers()) {
                            player.teleport(Utils.getSpawnPoint());
                        }

                        player.playSound(player.getLocation(), Utils.FinishPlotSound, 1, 1);
                    } else if (event.getCurrentItem().equals(itemMap)) {
                        event.getWhoClicked().closeInventory();
                        new PlotActionsMenu(selectedPlot, player);
                    } else {
                        int slot = event.getSlot();
                        int column = (slot % 9) + 1;
                        int row = (slot - (slot % 9)) / 9 + 1;

                        ItemMeta meta = event.getCurrentItem().getItemMeta();

                        if (column > 2 && column < 9 && row > 1 && row < 6) {
                            if (meta.hasEnchant(Enchantment.ARROW_DAMAGE)) {
                                meta.removeEnchant(Enchantment.ARROW_DAMAGE);
                            } else {
                                //Go through the whole points row
                                for (int i = 0; i < 6; i++) {
                                    if (reviewPlotMenu.getItem(slot - (column - 1) + i + 2).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE)) {
                                        ItemMeta metaPrevious = reviewPlotMenu.getItem(slot - (column - 1) + i + 2).getItemMeta();
                                        metaPrevious.removeEnchant(Enchantment.ARROW_DAMAGE);
                                        reviewPlotMenu.getItem(slot - (column - 1) + i + 2).setItemMeta(metaPrevious);
                                    }
                                }

                                meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                            }
                            event.getCurrentItem().setItemMeta(meta);
                            reviewPlotMenu.setItem(slot, event.getCurrentItem());
                        }
                    }
                    event.setCancelled(true);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Plot getPlotByValue(ItemStack item) {
        for (Map.Entry entry : plotItems.entrySet()) {
            if (entry.getValue().equals(item)) {
                return (Plot) entry.getKey();
            }
        }
        return null;
    }

    public static ItemStack getItem(){
        return new ItemBuilder(Material.BOOK, 1)
                .setName("§b§lReview Plots §7(Right Click)")
                .setEnchantment(Enchantment.ARROW_DAMAGE)
                .setItemFlag(ItemFlag.HIDE_ENCHANTS)
                .build();
    }
}
