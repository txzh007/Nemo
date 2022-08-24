package link.tanxin.template.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 模板加载类
 *
 * @author tan
 * @date 2022年07月24日
 */

@Controller
public class IndexController {
    @RequestMapping("index")
    public String preIndex(Model model) {

        return "index";
    }
}
