package utils.message;
public enum MessageType {
    DMA("dMalloc"),
    DAW("dAccessWrite"),
    DAR("dAccessRead"),
    DRE("dRelease"),
    DFR("dFree"),
    EXP("dException"), // 代表不同的命令类型，如dMalloc, dAccessWrite等
    HBM("HeartbeatMessage"); //心跳

    private final String description;

    // 构造函数用于关联常量和值
    private MessageType(String description) {
        this.description = description;
    }

    // 获取常量关联的值
    public String getDescription() {
        return description;
    }
}

