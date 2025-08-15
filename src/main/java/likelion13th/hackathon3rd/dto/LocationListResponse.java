package likelion13th.hackathon3rd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationListResponse {
    
    private Integer id;
    private String name;
    private List<String> picture;
    private String address;
    private Boolean advertisement;
} 