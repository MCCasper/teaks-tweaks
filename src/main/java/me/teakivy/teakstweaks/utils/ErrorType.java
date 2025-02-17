package me.teakivy.teakstweaks.utils;

public enum ErrorType {
    UNKNOWN_ERROR(MessageHandler.getMessage("plugin.error.unknown")),
    PACK_NOT_ENABLED(MessageHandler.getMessage("plugin.error.pack-not-enabled")),
    NOT_PLAYER(MessageHandler.getMessage("plugin.error.not-player")),
    NOT_OP(MessageHandler.getMessage("plugin.error.no-op")),
    MISSING_COMMAND_PERMISSION(MessageHandler.getMessage("plugin.error.missing-command-permission")),
    MISSING_PERMISSION(MessageHandler.getMessage("plugin.error.missing-permission")),
    CANT_GET_LATEST(MessageHandler.getMessage("plugin.error.cant-get-latest")),
    COMMAND_DISABLED(MessageHandler.getMessage("plugin.error.command-disabled")),
    MISSING_ACTION(MessageHandler.getMessage("plugin.error.missing-action"));


    final String text;

    ErrorType(final String error) {
        this.text = error;
    }

    public String m() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
