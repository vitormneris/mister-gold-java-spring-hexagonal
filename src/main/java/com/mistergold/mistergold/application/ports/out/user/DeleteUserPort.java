package com.mistergold.mistergold.application.ports.out.user;

import com.mistergold.mistergold.application.domain.user.User;

public interface DeleteUserPort {
    void delete(String id);
    User inactivate(String id);
}
