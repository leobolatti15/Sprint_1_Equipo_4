package com.example.Sprint1Equipo4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
   private String description;
   private int status;

}

//public class ErrorDTO {
//
//   private String description;
//   @JsonProperty("messages_list")
//   private List<String> messagesList;
//
//}