package com.github.rightshiro.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.rightshiro.model.entity.AuthResourceDO;

/**
 * @author weiguangchao
 * @date 2020/11/13
 */
@Repository
public interface AuthResourceRepository extends JpaRepository<AuthResourceDO, Long> {

    @Query("select res from AuthResourceDO res where res.type=1 and not exists ("
            + "select 1 from AuthRoleResourceDO rr "
            + "where rr.resourceId=res.id and rr.roleId=?1"
            + ")")
    Page<AuthResourceDO> listNotAuthorityMenuByRoleId(Long roleId, Pageable pageable);

    @Query("select res from AuthResourceDO res "
            + "inner join AuthRoleResourceDO rr on rr.resourceId=res.id "
            + "where res.type=1 and rr.roleId=?1")
    Page<AuthResourceDO> listAuthorityMenuByRoleId(Long roleId, Pageable pageable);

    @Query("select res from AuthResourceDO res where res.type=2 and not exists ("
            + "select 1 from AuthRoleResourceDO rr "
            + "where rr.resourceId=res.id and rr.roleId=?1"
            + ")")
    Page<AuthResourceDO> listNotAuthorityApiByRoleId(Long roleId, Pageable pageable);

    @Query("select res from AuthResourceDO res "
            + "inner join AuthRoleResourceDO rr on rr.resourceId=res.id "
            + "where res.type=2 and rr.roleId=?1")
    Page<AuthResourceDO> listAuthorityApiByRoleId(Long roleId, Pageable pageable);

    @Query("select res from AuthUserDO user "
            + "inner join AuthUserRoleDO ur on user.uid=ur.userId "
            + "inner join AuthRoleResourceDO rr on rr.roleId=ur.roleId "
            + "inner join AuthResourceDO res on res.id=rr.resourceId "
            + "where user.uid=?1 and res.type=1 and res.status=1")
    List<AuthResourceDO> listAuthorityMenuByUid(String uid);

    @Query("select r from AuthResourceDO r "
            + "where r.type in (2,3) and r.parentId=?1 and r.parentId=?1")
    Page<AuthResourceDO> listApiByTeamId(Long teamId, Pageable pageable);

    List<AuthResourceDO> getByType(Integer type);

    Page<AuthResourceDO> getByType(Integer type, Pageable pageable);

    /**
     * [0]：resource uri method (/aaa/bbb==GET)
     * [1]：role
     */
    @Query("select concat(r.uri,'==',upper(r.method)),role.code from AuthResourceDO r "
            + "left join AuthRoleResourceDO re on r.id=re.resourceId "
            + "left join AuthRoleDO role on role.id=re.roleId "
            + "where r.type=2")
    List<Object[]> listResAndRole();
}
