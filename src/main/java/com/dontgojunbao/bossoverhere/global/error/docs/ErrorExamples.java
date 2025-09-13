package com.dontgojunbao.bossoverhere.global.error.docs;

public final class ErrorExamples {
    private ErrorExamples() {}

    // --- Common ---
    public static final String INVALID_PARAMETER = """
    {
      "code": "INVALID_PARAMETER",
      "message": "유효하지 않은 요청 파라미터가 포함되어 있습니다.",
      "errors": [
        { "field": "startAt", "message": "must not be null" }
      ]
    }""";

    public static final String UNAUTHORIZED = """
    { "code": "UNAUTHORIZED", "message": "인증이 필요합니다." }
    """;

    public static final String FORBIDDEN = """
    { "code": "FORBIDDEN", "message": "권한이 없습니다." }
    """;

    public static final String RESOURCE_NOT_FOUND = """
    { "code": "RESOURCE_NOT_FOUND", "message": "요청한 리소스를 찾을 수 없습니다." }
    """;

    public static final String INTERNAL_SERVER_ERROR = """
    { "code": "INTERNAL_SERVER_ERROR", "message": "서버 오류가 발생했습니다." }
    """;

    // --- Security ---
    public static final String MISSING_ACCESS_TOKEN = """
    { "code": "MISSING_ACCESS_TOKEN", "message": "액세스 토큰이 없습니다." }
    """;
    public static final String INVALID_ACCESS_TOKEN = """
    { "code": "INVALID_ACCESS_TOKEN", "message": "유효하지 않은 액세스 토큰입니다." }
    """;
    public static final String EXPIRED_ACCESS_TOKEN = """
    { "code": "EXPIRED_ACCESS_TOKEN", "message": "액세스 토큰이 만료되었습니다." }
    """;
    public static final String MISSING_REFRESH_TOKEN = """
    { "code": "MISSING_REFRESH_TOKEN", "message": "리프레시 토큰이 없습니다." }
    """;
    public static final String INVALID_REFRESH_TOKEN = """
    { "code": "INVALID_REFRESH_TOKEN", "message": "유효하지 않은 리프레시 토큰입니다." }
    """;
    public static final String EXPIRED_REFRESH_TOKEN = """
    { "code": "EXPIRED_REFRESH_TOKEN", "message": "리프레시 토큰이 만료되었습니다." }
    """;
    public static final String ACCESS_DENIED = """
    { "code": "ACCESS_DENIED", "message": "권한이 없습니다." }
    """;

    // --- Domain ---
    public static final String NOT_FOUND_POST = """
    { "code": "NOT_FOUND_POST", "message": "기록을 찾을 수 없습니다." }
    """;
    public static final String FORBIDDEN_POST = """
    { "code": "FORBIDDEN_POST", "message": "해당 기록에 대한 변경 권한이 없습니다." }
    """;
    public static final String BAD_WORD_DETECTED = """
    { "code": "BAD_WORD_DETECTED", "message": "비속어는 문구에 포함할 수 없습니다." }
    """;
    public static final String NOTFOUND_SPOT = """
    { "code": "NOTFOUND_SPOT", "message": "해당 스팟를 찾을 수 없습니다." }
    """;
    public static final String NOTFOUND_FOOD_CATEGORY = """
    { "code": "NOTFOUND_FOOD_CATEGORY", "message": "해당 음식을 찾을 수 없습니다." }
    """;
    public static final String NOTFOUND_CLUSTER = """
    { "code": "NOTFOUND_CLUSTER", "message": "해당 클러스터를 찾을 수 없습니다." }
    """;
    public static final String INVALID_CLUSTER_SELECTION = """
    { "code": "INVALID_CLUSTER_SELECTION", "message": "선택하신 클러스터가 해당 음식 카테고리에 매핑되어 있지 않습니다." }
    """;
    public static final String NOTFOUND_USER = """
    { "code": "NOTFOUND_USER", "message": "해당 사용자를 찾을 수 없습니다." }
    """;
    public static final String AI_CALL_FAILURE = """
    { "code": "AI_CALL_FAILURE", "message": "AI 추천 서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요." }
    """;
}
