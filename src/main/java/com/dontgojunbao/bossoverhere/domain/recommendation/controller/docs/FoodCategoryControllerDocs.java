package com.dontgojunbao.bossoverhere.domain.recommendation.controller.docs;

import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.ClusterDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.FoodCategoryDetailResponse;
import com.dontgojunbao.bossoverhere.global.common.dto.CommonResponse;
import com.dontgojunbao.bossoverhere.global.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FoodCategoryControllerDocs {

    @Operation(summary = "모든 음식 카테고리 + 매핑된 클러스터 ID 조회", tags = "Food-Category",
            description = "로그인 사용자의 인증을 확인한 후, 전체 음식 카테고리 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success",
                                    value = """
                                    {
                                      "success": true,
                                      "data": [
                                        {
                                          "foodCategoryId": 1,
                                          "name": "분식",
                                          "description": null,
                                          "clusterIds": [1,3,4,6]
                                        },
                                        {
                                          "foodCategoryId": 2,
                                          "name": "한식",
                                          "description": "덮밥류, 제육, 불고기 등",
                                          "clusterIds": [1,2,3,6]
                                        }
                                      ]
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
    ResponseEntity<CommonResponse<List<FoodCategoryDetailResponse>>> getFoodCategories(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId
    );

    @Operation(summary = "카테고리별 클러스터 조회", tags = "Food-Category",
            description = "특정 음식 카테고리에 매핑된 클러스터 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class),
                            examples = @ExampleObject(name = "success",
                                    value = """
                                    {
                                      "success": true,
                                      "data": [
                                        {
                                          "clusterId": 1,
                                          "title": "동네 일상과 젊음의 중심",
                                          "nickname": "활력 가득한 생활권 거점",
                                          "description": "10–30대 비율이 높고 평일 방문자 수가 많은 지역",
                                          "situations": "출퇴근 후 산책, 운동, 친구와의 약속"
                                        },
                                        {
                                          "clusterId": 4,
                                          "title": "운동 모임 스팟",
                                          "nickname": "7~8월 특화 액티비티 존",
                                          "description": "방학 시즌, 특정 시간대 집중",
                                          "situations": "수영, 운동, 짧은 모임"
                                        }
                                      ]
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
            @ApiResponse(responseCode = "404", description = "해당 음식 카테고리 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name="NOTFOUND_FOOD_CATEGORY",
                                    value = """
                                            { "code":"NOTFOUND_FOOD_CATEGORY","message":"해당 음식을 찾을 수 없습니다." }""")))
            ,
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name="INTERNAL_SERVER_ERROR",
                                    value = """
                                            { "code":"INTERNAL_SERVER_ERROR","message":"서버 오류가 발생했습니다." }""")))
    })
    ResponseEntity<CommonResponse<List<ClusterDetailResponse>>> getClustersByCategory(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId,
            @Parameter(description = "카테고리 ID", example = "1") Long categoryId
    );
}