package com.tacs.rest.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.tacs.rest.entity.User;

@Repository
public interface UserDAO extends PagingAndSortingRepository<User, Integer> {

	List<User> findByUsername(String username);

	List<User> findByTelegramChatId(long telegram_chat_id);
	List<User> findByCountriesList_idCountriesList(int idCountriesList);

	@Query(value = "SELECT * FROM public.USER WHERE UPPER(USERNAME) LIKE %:filter% OR UPPER(LASTNAME) LIKE %:filter% OR UPPER(FIRSTNAME) LIKE %:filter%", 
	countQuery = "SELECT count(*) FROM public.USER WHERE UPPER(USERNAME) LIKE %:filter% OR UPPER(LASTNAME) LIKE %:filter% OR UPPER(FIRSTNAME) LIKE %:filter%", 
	nativeQuery = true)
	Page<User> fitlerUsersByString(String filter, Pageable pageable);
	
	@Query(value = "SELECT * FROM public.USER u INNER JOIN public.COUNTRIESLIST c ON c.id_user = u.id_user"
			+ " WHERE u.telegram_chat_id = :telegram_chat_id", 
			nativeQuery = true)
	User findUserAndListsByTelegramChatId(long telegram_chat_id);
}
