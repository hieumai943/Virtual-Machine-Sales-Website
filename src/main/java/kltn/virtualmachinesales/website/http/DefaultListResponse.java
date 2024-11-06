package kltn.virtualmachinesales.website.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultListResponse<T> {
    private Boolean success;
    private String message;
    private List<T> data;
    @JsonProperty("total_record")
    private Long totalRecord;

    public static <T> ResponseEntity<DefaultListResponse<T>> success(List<T> data, Long totalRecord) {
        return new ResponseEntity<>(DefaultListResponse.<T>builder()
                .success(true)
                .data(data)
                .totalRecord(totalRecord)
                .build(), HttpStatus.OK);
    }

    public static <T> ResponseEntity<DefaultListResponse<T>> error(String message) {
        return new ResponseEntity<>(DefaultListResponse.<T>builder()
                .success(false)
                .message(message)
                .build(), HttpStatus.OK);
    }
}
