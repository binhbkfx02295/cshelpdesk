package com.binhbkfx02295.cshelpdesk.openai.adapter;

import com.binhbkfx02295.cshelpdesk.openai.config.ModelRegistryConfig;
import com.binhbkfx02295.cshelpdesk.openai.model.ModelSettings;
import com.binhbkfx02295.cshelpdesk.repository.CategoryRepository;
import com.binhbkfx02295.cshelpdesk.repository.EmotionRepository;
import com.binhbkfx02295.cshelpdesk.repository.SatisfactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Getter
@Setter
public class GPT41MiniAdapter extends BaseGPTModelAdapter{
    private final ModelSettings modelSettings;

    @Autowired
    public GPT41MiniAdapter(ModelRegistryConfig modelRegistryConfig,
                            RestTemplate restTemplate,
                            ObjectMapper objectMapper,
                            CategoryRepository categoryRepository,
                            SatisfactionRepository satisfactionRepository,
                            EmotionRepository emotionRepository) {
        super(restTemplate, objectMapper, categoryRepository, satisfactionRepository, emotionRepository);
        modelSettings = modelRegistryConfig.getGpt41Mini();
    }

    @Override
    public ModelSettings getModelSettings() {
        return modelSettings;
    }
}
