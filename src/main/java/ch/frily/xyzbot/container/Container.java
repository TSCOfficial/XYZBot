package ch.frily.xyzbot.container;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.Component;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class Container {

    @Setter
    @Getter
    private List<ContainerChildComponent> components = new ArrayList<>();

    @Getter
    @Setter
    private List<net.dv8tion.jda.api.components.container.Container> containers = new ArrayList<>();

    /**
     * Add a component to the container<br>
     * Available components:
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.components.section.Section}</li>
     *     <li>{@link net.dv8tion.jda.api.components.textdisplay.TextDisplay}</li>
     *     <li>{@link net.dv8tion.jda.api.components.mediagallery.MediaGallery}</li>
     *     <li>{@link net.dv8tion.jda.api.components.buttons.Button}</li>
     *     <li>{@link net.dv8tion.jda.api.components.actionrow.ActionRow}</li>
     *     <li>...</li>
     * </ul>
     * <a href="https://docs.discord.com/developers/components/reference">Discord components reference</a>
     * @param component
     * @return
     */
    public List<ContainerChildComponent> addComponent(ContainerChildComponent component) {
        components.add(component);
        return components;
    }

    public List<ContainerChildComponent> addComponents(List<ContainerChildComponent> components) {
        this.components.addAll(components);
        return this.components;
    }

    public List<net.dv8tion.jda.api.components.container.Container> addContainer(net.dv8tion.jda.api.components.container.Container container){
        this.containers.add(container);
        return this.containers;
    }

    public List<net.dv8tion.jda.api.components.container.Container> build() {
        log.debug("Building containers...");
        if (!components.isEmpty()) {
            log.debug("Components detected: {}", components.size());
            List<ContainerChildComponent> newComponents = new ArrayList<>();

            for (int i = 0; i < components.size(); i++) {
                if (i > 0 && i % 40 == 0) {
                    log.debug("Triggered new container");
                    net.dv8tion.jda.api.components.container.Container newContainer =
                            net.dv8tion.jda.api.components.container.Container.of(newComponents);
                    containers.add(newContainer);
                    newComponents.clear();
                }
                newComponents.add(components.get(i));
            }

            // Add any remaining components to a new container
            if (!newComponents.isEmpty()) {
                log.debug("Adding remaining components as a new container");
                net.dv8tion.jda.api.components.container.Container newContainer =
                        net.dv8tion.jda.api.components.container.Container.of(newComponents);
                containers.add(newContainer);
            }
        }
        log.debug("Returning containers: {}", containers.size());
        return containers;
    }

}
