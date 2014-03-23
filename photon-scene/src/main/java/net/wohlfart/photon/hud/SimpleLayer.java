package net.wohlfart.photon.hud;


import javax.vecmath.Vector3f;

import net.wohlfart.photon.hud.layout.Container;
import net.wohlfart.photon.hud.layout.CornerConstraints;
import net.wohlfart.photon.hud.layout.CornerLayoutStrategy;
import net.wohlfart.photon.hud.layout.FlowContraints;
import net.wohlfart.photon.hud.layout.FlowLayoutStrategy;
import net.wohlfart.photon.hud.layout.Panel;
import net.wohlfart.photon.tools.Dimension;
import net.wohlfart.photon.tools.Quaternion;

public class SimpleLayer extends AbstractLayer<CornerConstraints> {

    private float time;

    private final Chart<CornerConstraints> memoryChart = new Chart<CornerConstraints>(100,70);
    private final Label<CornerConstraints> memoryLabel = new Label<CornerConstraints>("");

    private final Chart<CornerConstraints> fpsChart = new Chart<CornerConstraints>(100,70);
    private final Label<CornerConstraints> fpsLabel = new Label<CornerConstraints>("");

    Dimension dim = new Dimension(150, 30);
    FlowLayoutStrategy flowLayout = new FlowLayoutStrategy();
    private final Panel<CornerConstraints, FlowContraints> panel = new Panel<CornerConstraints, FlowContraints>(dim, flowLayout);


    public SimpleLayer(Dimension dimension) {
        super(dimension, new CornerLayoutStrategy());

        panel.add(new Label<FlowContraints>("test1"));
        panel.add(new Label<FlowContraints>("test2"));
        panel.add(new Label<FlowContraints>("test3"));

        Container<CornerConstraints> container = getContainer();
        container.add(memoryChart).alignToTopLeft().withBorder(2);
        container.add(memoryLabel).alignToTopLeft().withBorder(2);
        container.add(fpsChart).alignToTopLeft().withBorder(2);
        container.add(fpsLabel).alignToTopLeft().withBorder(2);

        container.add(panel).alignToTopMiddle();
    }

    @Override
    public void update(Quaternion rot, Vector3f mov, float delta) {
        time += delta;
        if (time > 0.5) {
            updateMemory(delta);
            updateFps(delta);
            time = 0;
        }
    }

    private void updateFps(float delta) {
        float val = Math.min(1f / delta, 200f);
        fpsChart.addData(Math.min(val, 200f) / 200f);
        fpsLabel.setText(String.format("fps: %1.2f", val));
    }

    private void updateMemory(float delta) {
        double maxMem = Runtime.getRuntime().maxMemory();
        double freeMem = Runtime.getRuntime().freeMemory();
        float mem = 1 - (float)((maxMem - freeMem) / maxMem);
        memoryChart.addData(mem);
        memoryLabel.setText(String.format("MByte : %1.2f", (maxMem - freeMem) / 1024 / 1024 ));
    }

}
