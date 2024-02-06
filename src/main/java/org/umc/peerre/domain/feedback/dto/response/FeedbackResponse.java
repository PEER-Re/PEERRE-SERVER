package org.umc.peerre.domain.feedback.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.umc.peerre.domain.feedback.dto.request.FeedbackRequest;

public class FeedbackResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class yesFeedback {
        private Boolean goodCommunication;
        private Boolean punctual;
        private Boolean competent;
        private Boolean articulate;
        private Boolean thorough;
        private Boolean engaging;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class noFeedback {
        private Boolean badCommunication;
        private Boolean notPunctual;
        private Boolean notCompetent;
        private Boolean notArticulate;
        private Boolean notThorough;
        private Boolean notEngaging;
    }

}
