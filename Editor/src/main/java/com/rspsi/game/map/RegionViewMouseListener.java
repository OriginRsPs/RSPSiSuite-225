package com.rspsi.game.map;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.jagex.Client;
import com.jagex.cache.loader.map.MapIndexLoader;
import com.rspsi.ui.EditRegionsWindow;

import com.rspsi.ui.MainWindow;
import com.rspsi.ui.MultiRegionMapWindow;
import com.rspsi.ui.PickHashWindow;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegionViewMouseListener implements MouseListener {

    private EditRegionsWindow editRegions;

    public RegionViewMouseListener() {
        editRegions = new EditRegionsWindow();
        try {
            editRegions.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        if (arg0.getComponent() instanceof RegionView view) {
            view.setHovered(true);
        }
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        if (arg0.getComponent() instanceof RegionView view) {
            view.setHovered(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        if (arg0.getComponent() instanceof RegionView selectedRegion) {
            if (arg0.getButton() == MouseEvent.BUTTON3) {
                for (Component comp : selectedRegion.getParent().getComponents()) {
                    if (comp instanceof RegionView rv) {
                        rv.setSelected(false);
                    }
                }

                selectedRegion.setSelected(true);
                JPopupMenu popup = new JPopupMenu();
                final JMenuItem editRegion = getEditRegion(selectedRegion);
                final JMenuItem loadRegion = getLoadRegion(selectedRegion);
                popup.add(editRegion);
                popup.add(loadRegion);
                popup.show(arg0.getComponent(), arg0.getX(), arg0.getY());
            } else {
                if (!arg0.isShiftDown()) {
                    for (Component comp : selectedRegion.getParent().getComponents()) {
                        if (comp instanceof RegionView rv) {
                            rv.setSelected(false);
                        }
                    }
                }
                selectedRegion.setSelected(true);
            }

        }
    }

    private static JMenuItem getLoadRegion(RegionView selectedRegion) {
        final JMenuItem loadRegion = new JMenuItem("Load Region");
        loadRegion.addActionListener(al -> {
            final MainWindow window = MainWindow.getSingleton();
            final PickHashWindow pickHash = window.getPickHash();
            pickHash.setHashText(new TextField(String.valueOf(selectedRegion.hash)));
            final int hash = pickHash.getHash();
            Platform.runLater(() -> {
                Client.getSingleton().loadCoordinates((hash >> 8) * 64, (hash & 0xff) * 64, pickHash.getLength(), pickHash.getWidth());
                try {
                    window.fullMapView.start(new Stage());
                    window.fullMapView.resizeMap();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });
        return loadRegion;
    }

    private JMenuItem getEditRegion(RegionView selectedRegion) {
        final JMenuItem editRegion = new JMenuItem("Edit region");
        editRegion.addActionListener(al ->
                Platform.runLater(() -> {
                    editRegions.show(selectedRegion);
                    if (editRegions.valid()) {
                        System.out.println("LS: " + editRegions.getLandscapeId() + " OBJ: " + editRegions.getObjectId());
                        MapIndexLoader.setRegionData(selectedRegion.getRegionX(), selectedRegion.getRegionY(), editRegions.getLandscapeId(), editRegions.getObjectId());
                        selectedRegion.images = null;
                        selectedRegion.loadMap();
                        selectedRegion.invalidate();
                    }
                }));
        return editRegion;
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

        System.out.println("mouse released!");
    }


}
