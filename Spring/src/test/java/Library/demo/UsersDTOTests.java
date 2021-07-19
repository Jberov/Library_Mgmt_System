package Library.demo;


import Library.demo.dto.UserDTO;
import Library.demo.entities.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserDTO.class)
public class UsersDTOTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDTO userDTO;

    @Test
    public void getUserTest() throws Exception{
        Users user = new Users("JBaller");
        BDDMockito.given(userDTO.getUser("JBaller")).willReturn(user);
        mvc.perform(MockMvcRequestBuilders.get("/users/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(user.getName())));

    }
}
