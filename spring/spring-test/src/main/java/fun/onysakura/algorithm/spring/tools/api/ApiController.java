package fun.onysakura.algorithm.spring.tools.api;

import fun.onysakura.algorithm.utils.core.basic.str.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
public class ApiController {

    @Autowired
    private ApiRepository apiRepository;

    @GetMapping("/content/{code}")
    public String get(@PathVariable("code") String code) {
        return apiRepository.findById(code).map(ApiModal::getContent).orElse(null);
    }

    @GetMapping("/{code}")
    public void redirect(@PathVariable("code") String code, HttpServletResponse response) throws Exception {
        Optional<String> contentOptional = apiRepository.findById(code).map(ApiModal::getContent);
        if (contentOptional.isPresent()) {
            response.sendRedirect(contentOptional.get());
        } else {
            PrintWriter writer = response.getWriter();
            writer.println("Null");
            writer.flush();
            writer.close();
        }
    }

    @PutMapping
    public String put(@RequestBody String content) {
        Optional<ApiModal> apiModalOptional = apiRepository.findByContent(content);
        ApiModal apiModal;
        if (apiModalOptional.isPresent()) {
            apiModal = apiModalOptional.get();
        } else {
            apiModal = new ApiModal();
            apiModal.setCode(RandomUtils.randomStrLowercase(4));
            apiModal.setContent(content);
            apiModal = apiRepository.saveAndFlush(apiModal);
        }
        return apiModal.getCode();
    }
}
