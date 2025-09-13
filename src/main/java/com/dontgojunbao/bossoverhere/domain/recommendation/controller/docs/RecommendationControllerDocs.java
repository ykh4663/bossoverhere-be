package com.dontgojunbao.bossoverhere.domain.recommendation.controller.docs;

import com.dontgojunbao.bossoverhere.domain.recommendation.dto.request.RecommendationRequestDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationDetailDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationSimpleDto;
import com.dontgojunbao.bossoverhere.global.common.dto.CommonResponse;
import com.dontgojunbao.bossoverhere.global.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RecommendationControllerDocs {

    @Operation(summary = "추천 요청", tags = "Recommendation",
            description = "문항/스팟/날짜·시간/선택 클러스터를 입력하면 추천 경로(세그먼트) 리스트를 반환합니다.")
    @RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = RecommendationRequestDto.class),
                    examples = @ExampleObject(name = "request",
                            value = """
                            {
                              "foodCategoryId": 1,
                              "spotId": 7,
                              "date": "2025-08-01",
                              "startTime": "09:00",
                              "endTime": "17:00",
                              "clusterIds": [1,4,6],
                              "answers": {
                                "q1": "혼잡도 낮은 곳",
                                "q2": "점심 피크 공략"
                              }
                            }
                            """)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성/추천 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success",
                                    value = """
                                    {
                                      "success": true,
                                      "data": [
                                        {
                                          "time": "09:00~10:30",
                                          "from": {
                                            "spotId": 7,
                                            "spotName": "왕송호수공원",
                                            "spotAddress": "경기도 의왕시 초평동 278-9",
                                            "spotLatitude": 37.3098252097,
                                            "spotLongitude": 126.9441405769
                                          },
                                          "to": {
                                            "spotId": 10,
                                            "spotName": "삼봉근린공원",
                                            "spotAddress": "경기도 화성시 봉담읍 상리 713",
                                            "spotLatitude": 37.225955,
                                            "spotLongitude": 126.944269
                                          }
                                        }
                                      ]
                                    }
                                    """)))
            ,
            @ApiResponse(responseCode = "400", description = "잘못된 요청/유효성 실패/클러스터 선택 불일치",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_PARAMETER",
                                            value = """
                                                    { "code":"INVALID_PARAMETER","message":"유효하지 않은 요청 파라미터가 포함되어 있습니다." }"""),
                                    @ExampleObject(name = "INVALID_CLUSTER_SELECTION",
                                            value = """
                                                    { "code":"INVALID_CLUSTER_SELECTION","message":"선택하신 클러스터가 해당 음식 카테고리에 매핑되어 있지 않습니다." }""")
                            }))
            ,
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_ACCESS_TOKEN",
                                            value = """
                                                    { "code":"INVALID_ACCESS_TOKEN","message":"유효하지 않은 액세스 토큰입니다." }"""),
                                    @ExampleObject(name = "EXPIRED_ACCESS_TOKEN",
                                            value = """
                                                    { "code":"EXPIRED_ACCESS_TOKEN","message":"액세스 토큰이 만료되었습니다." }""")
                            }))
            ,
            @ApiResponse(responseCode = "404", description = "리소스 없음(카테고리/스팟 등)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "NOTFOUND_FOOD_CATEGORY",
                                            value = """
                                                    { "code":"NOTFOUND_FOOD_CATEGORY","message":"해당 음식을 찾을 수 없습니다." }"""),
                                    @ExampleObject(name = "NOTFOUND_SPOT",
                                            value = """
                                                    { "code":"NOTFOUND_SPOT","message":"해당 스팟를 찾을 수 없습니다." }""")
                            }))
            ,
            @ApiResponse(responseCode = "500", description = "AI 호출 실패/서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "AI_CALL_FAILURE",
                                            value = """
                                                    { "code":"AI_CALL_FAILURE","message":"AI 추천 서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요." }"""),
                                    @ExampleObject(name = "INTERNAL_SERVER_ERROR",
                                            value = """
                                                    { "code":"INTERNAL_SERVER_ERROR","message":"서버 오류가 발생했습니다." }""")
                            }))
    })
    ResponseEntity<CommonResponse<List<RecommendationResponse>>> recommend(
            @Parameter(description = "추천 요청 본문") RecommendationRequestDto dto,
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId
    );

    @Operation(summary = "내 추천 요청 내역 조회(페이징)", tags = "Recommendation",
            description = "본인이 만든 추천 요청 이력을 페이지 단위로 조회합니다. 최신 날짜 순 정렬.")
    @Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "page", description = "페이지 번호(0-base)", example = "0"),
            @io.swagger.v3.oas.annotations.Parameter(name = "size", description = "페이지 크기", example = "10")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success-page",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "content": [
                                          {
                                            "requestId": 99,
                                            "date": "2025-08-01",
                                            "startTime": "09:00",
                                            "endTime": "17:00",
                                            "foodCategory": { "id": 1, "name": "분식" },
                                            "baseSpot": { "spotId": 7, "spotName": "왕송호수공원" },
                                            "createdAt": "2025-08-01T18:00:00"
                                          }
                                        ],
                                        "totalElements": 3,
                                        "totalPages": 1,
                                        "size": 10,
                                        "number": 0,
                                        "first": true,
                                        "last": true
                                      }
                                    }
                                    """)))
            ,
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_ACCESS_TOKEN",
                                            value = """
                                                    { "code":"INVALID_ACCESS_TOKEN","message":"유효하지 않은 액세스 토큰입니다." }"""),
                                    @ExampleObject(name = "EXPIRED_ACCESS_TOKEN",
                                            value = """
                                                    { "code":"EXPIRED_ACCESS_TOKEN","message":"액세스 토큰이 만료되었습니다." }""")
                            }))
            ,
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name="INTERNAL_SERVER_ERROR",
                                    value = """
                                            { "code":"INTERNAL_SERVER_ERROR","message":"서버 오류가 발생했습니다." }""")))
    })
    ResponseEntity<CommonResponse<Page<RecommendationSimpleDto>>> getHistory(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId,
            @Parameter(description = "페이지 번호(0-base)", example = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") int size
    );

    @Operation(summary = "추천 이력 상세 조회", tags = "Recommendation",
            description = "단일 추천 이력과 선택한 클러스터, 구간(세그먼트) 상세를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "requestId": 99,
                                        "date": "2025-08-01",
                                        "startTime": "09:00",
                                        "endTime": "17:00",
                                        "foodCategory": { "id": 1, "name": "분식" },
                                        "baseSpot": {
                                          "spotId": 7,
                                          "spotName": "왕송호수공원",
                                          "spotAddress": "경기도 의왕시 초평동 278-9",
                                          "spotLatitude": 37.3098252097,
                                          "spotLongitude": 126.9441405769
                                        },
                                        "selectedClusters": [
                                          { "clusterId": 1, "title": "동네 일상과 젊음의 중심" }
                                        ],
                                        "segments": [
                                          {
                                            "time": "09:00~10:30",
                                            "from": { "spotId": 7, "spotName": "왕송호수공원" },
                                            "to":   { "spotId": 10, "spotName": "삼봉근린공원" }
                                          }
                                        ],
                                        "createdAt": "2025-08-01T18:00:00"
                                      }
                                    }
                                    """)))
            ,
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "INVALID_ACCESS_TOKEN",
                                            value = """
                                                    { "code":"INVALID_ACCESS_TOKEN","message":"유효하지 않은 액세스 토큰입니다." }"""),
                                    @ExampleObject(name = "EXPIRED_ACCESS_TOKEN",
                                            value = """
                                                    { "code":"EXPIRED_ACCESS_TOKEN","message":"액세스 토큰이 만료되었습니다." }""")
                            }))
            ,
            @ApiResponse(responseCode = "403", description = "본인 이력 아님",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name="FORBIDDEN_HISTORY",
                                    value = """
                                            { "code":"FORBIDDEN_HISTORY","message":"본인 것이 아닌 추천 이력입니다." }""")))
            ,
            @ApiResponse(responseCode = "404", description = "이력 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name="NOT_FOUND_HISTORY",
                                    value = """
                                            { "code":"NOT_FOUND_HISTORY","message":"추천 이력을 찾을 수 없습니다." }""")))
            ,
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name="INTERNAL_SERVER_ERROR",
                                    value = """
                                            { "code":"INTERNAL_SERVER_ERROR","message":"서버 오류가 발생했습니다." }""")))
    })
    ResponseEntity<CommonResponse<RecommendationDetailDto>> getHistoryDetail(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId,
            @Parameter(description = "추천 요청 ID", example = "99") Long requestId
    );
}
