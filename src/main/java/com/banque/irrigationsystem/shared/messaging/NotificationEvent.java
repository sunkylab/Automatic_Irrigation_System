
package com.banque.irrigationsystem.shared.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class NotificationEvent {
    private String message;
}
