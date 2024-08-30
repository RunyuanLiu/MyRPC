package part1.common.service.Impl;

import part1.common.service.BlogService;
import part1.common.pojo.Blog;

/**
 * @ClassName BlogServiceImpl
 * @Description TODO
 * @Author 氟西汀
 * @Date 2024/6/26 9:59
 * @Version 1.0
 */

public class BlogServiceImpl implements BlogService {
    @Override
    public Blog getBlogById(Integer id) {
        Blog blog = Blog.builder().id(id).title("我的博客").userId(22).build();
        System.out.println("客户端查询了"+id+"博客");
        return blog;
    }
}
