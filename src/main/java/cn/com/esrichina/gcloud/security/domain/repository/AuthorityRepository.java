package cn.com.esrichina.gcloud.security.domain.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.com.esrichina.gcloud.commons.basedao.BaseRepository;
import cn.com.esrichina.gcloud.security.domain.Authority;

@Repository
@Transactional
public class AuthorityRepository extends BaseRepository<Authority> {
}
