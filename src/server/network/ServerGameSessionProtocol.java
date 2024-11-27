package server.network;

public enum ServerGameSessionProtocol {
    WAITING,
    SENT_CATEGORY,
    SENT_CATEGORY_TO_OPPONENT,
    SENT_QUESTIONS,
    SENT_ROUND_SCORE,
    GAME_OVER,
    SEND_SCORE_WINDOW_DATA,
    PLAYER_GAVE_UP,
    PLAY_AGAIN_SUCCESS,
    PLAY_AGAIN_DENIED, PRE_QUESTIONS_CHECK,
}