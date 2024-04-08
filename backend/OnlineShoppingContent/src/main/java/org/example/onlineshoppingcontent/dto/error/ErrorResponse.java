package org.example.onlineshoppingcontent.dto.error;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
}
