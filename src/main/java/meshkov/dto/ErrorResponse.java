package meshkov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String error;
    private String time;
    private UUID errorId;

    public ErrorResponse(String error) {
        this.error = error;
        this.time = LocalDateTime.now().toString();
        this.errorId = UUID.randomUUID();
    }
}
