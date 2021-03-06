package omni.encyclopedia.pipelines.worker;

import bwapi.UnitType;
import omni.encyclopedia.Entity;
import omni.encyclopedia.map.OmniMap;
import omni.encyclopedia.self.Minerals;
import omni.encyclopedia.actions.TrainAction;
import omni.encyclopedia.generic.Producer;
import omni.encyclopedia.self.Self;
import omni.flow.Pipeline;
import omni.flow.Potential;
import omni.flow.scoring.Score;

import java.util.ArrayList;
import java.util.List;

public class BuildWorkerPipeline extends Pipeline {
    @Override
    protected List<Potential> getPotentials(List<Entity> entities, Self self, OmniMap map) {
        List<Potential> potentials = new ArrayList<>();
        // Get total amount of minerals, get total amount of worker production queues available.
        // TODO: Build from production facility available (closest to open mineral fields?)
        // TODO: Reusable itermediate pipeline results for cacheing computed things like closest open mineral fields.
        List<Producer> producers = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Producer) {
                // TODO: Need to filter on those that can produce workers.
                Producer producer = (Producer) entity;
                if (producer.canProduce()) {
                    producers.add(producer);
                }
            }
        }

        if (self.getMinerals().getCount() >= 50) {
            for (Producer producer : producers) {
                potentials.add(new Potential(Score.max(),
                        new TrainAction(producer, UnitType.Zerg_Drone),
                        producer));
            }
        }

        return potentials;
    }
}
