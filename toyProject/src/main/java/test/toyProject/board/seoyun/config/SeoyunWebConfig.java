package test.toyProject.board.seoyun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class SeoyunWebConfig implements WebMvcConfigurer {
    private String resourcePath = "/upload/**"; // view에서 접근할 경로
    private String savePath = "file:///C:/SpringBootStudy/CONCAT_toyProject/boardImg/"; // 실제 파일 저장 경로(win)

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }
}
