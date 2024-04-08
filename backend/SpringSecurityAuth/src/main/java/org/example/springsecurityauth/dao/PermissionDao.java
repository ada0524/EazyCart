package org.example.springsecurityauth.dao;

import org.example.springsecurityauth.entity.Permission;
import org.example.springsecurityauth.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class PermissionDao extends AbstractHibernateDao {

    public PermissionDao() {
        setClazz(PermissionDao.class);
    }

    public void savePermission(Permission permission) {
        this.save(permission);
    }

    public Set<Permission> createUserDefaultPermission(User user, String[] permissionValues) {
        Set<Permission> permissions = new HashSet<>();

        for (String permissionValue : permissionValues) {
            Permission permission = new Permission();
            permission.setValue(permissionValue);
            permission.setUser(user);

            savePermission(permission);
            permissions.add(permission);
        }

        return permissions;
    }
}
