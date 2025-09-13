package com.dontgojunbao.bossoverhere.domain.spot.controller.docs;

import com.dontgojunbao.bossoverhere.domain.spot.dto.SpotDto;
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

public interface SpotControllerDocs {

    @Operation(summary = "전체 스팟 조회", description = "모든 스팟 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(name = "OK", value = """
            {
              "success": true,
              "data": [
                {
                  "spotId": 1,
                  "spotName": "서서울톨게이트",
                  "spotAddress": "경기도 ...",
                  "spotLatitude": 37.3566,
                  "spotLongitude": 126.8648
                }
              ]
            }
            """)
            )),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                            @ExampleObject(name = "INVALID_ACCESS_TOKEN", value = """
                { "code": "INVALID_ACCESS_TOKEN", "message": "유효하지 않은 액세스 토큰입니다." }
                """),
                            @ExampleObject(name = "MISSING_ACCESS_TOKEN", value = """
                { "code": "MISSING_ACCESS_TOKEN", "message": "액세스 토큰이 없습니다." }
                """)
                    }
            )),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(name = "INTERNAL_SERVER_ERROR", value = """
            { "code": "INTERNAL_SERVER_ERROR", "message": "서버 오류가 발생했습니다." }
            """)
            ))
    })
    ResponseEntity<CommonResponse<List<SpotDto>>> getAllSpots(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId);

    @Operation(summary = "스팟 단건 조회", description = "spotId로 스팟 한 건을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(name = "OK", value = """
            {
              "success": true,
              "data": {
                "spotId": 1,
                "spotName": "서서울톨게이트",
                "spotAddress": "경기도 ...",
                "spotLatitude": 37.3566,
                "spotLongitude": 126.8648
              }
            }
            """)
            )),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(name = "INVALID_ACCESS_TOKEN", value = """
            { "code": "INVALID_ACCESS_TOKEN", "message": "유효하지 않은 액세스 토큰입니다." }
            """)
            )),
            @ApiResponse(responseCode = "404", description = "스팟 없음", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(name = "NOTFOUND_SPOT", value = """
            { "code": "NOTFOUND_SPOT", "message": "해당 스팟를 찾을 수 없습니다." }
            """)
            )),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(name = "INTERNAL_SERVER_ERROR", value = """
            { "code": "INTERNAL_SERVER_ERROR", "message": "서버 오류가 발생했습니다." }
            """)
            ))
    })
    ResponseEntity<CommonResponse<SpotDto>> getSpot(
            @Parameter(hidden = true, description = "인증된 사용자 ID") Long userId,
            @Parameter(description = "스팟 ID", example = "1") Long spotId);
}