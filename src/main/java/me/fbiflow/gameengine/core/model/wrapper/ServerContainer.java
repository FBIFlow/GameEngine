package me.fbiflow.gameengine.core.model.wrapper;

import me.fbiflow.gameengine.core.model.wrapper.internal.view.BossbarView;
import me.fbiflow.gameengine.core.model.wrapper.internal.view.ChatView;
import me.fbiflow.gameengine.core.model.wrapper.internal.view.SidebarView;

import static java.lang.String.format;

@SuppressWarnings("unchecked")
public class ServerContainer {

    private final ContainerType containerType;

    private final String pathModifier;

    public final Class<? extends BossbarView> bossbarView;
    public final Class<? extends ChatView> chatView;
    public final Class<? extends SidebarView> sidebarView;


    public ServerContainer(ContainerType containerType) {
        this.containerType = containerType;
        this.pathModifier = containerType.path;

        this.bossbarView = (Class<? extends BossbarView>) getImpl("BossbarView");
        this.chatView = (Class<? extends ChatView>) getImpl("ChatView");
        this.sidebarView = (Class<? extends SidebarView>) getImpl("SidebarView");
    }

    public InstanceFabric createInstanceFabric() {
        return new InstanceFabric(this);
    }

    public ContainerType getContainerType() {
        return this.containerType;
    }

    private Class<?> getImpl(String className) {
        try {
            return Class.forName(format("me.fbiflow.gameengine.core.model.wrapper.%s.view.%s%s",
                    this.pathModifier.toLowerCase(), this.pathModifier, className
            ));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public enum ContainerType {

        BUKKIT("Bukkit"), MINESTOM("Minestom");

        public final String path;

        ContainerType(String path) {
            this.path = path;
        }

    }
}