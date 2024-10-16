package test.toyProject.board.yoonseo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class YoonseoWebConfig implements WebMvcConfigurer {
    private String resourcePath = "/upload/**"; // view 에서 접근할 경로
    private String savePath = "file:///C:/SpringBootStudy/CONCAT_toyProject/boardImg/"; // 실제 파일 저장 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }

}
