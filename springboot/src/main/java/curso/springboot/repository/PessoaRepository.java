package curso.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	@Query("select p from Pessoa p where p.nome like %?1% ")
	List<Pessoa> findPessoaByName(String nome);
	
	@Query("select p from Pessoa p where p.sexopessoa = ?1 ")
	List<Pessoa> findPessoaBySexo(String sexo);
	
	@Query("select p from Pessoa p where p.nome like %?1% and p.sexopessoa = ?2")
	List<Pessoa> findPessoaByNameSexo(String nome, String sexopessoa);
	
	
	default Page<Pessoa> findPessoaByNamePage(String nome, Pageable pageable) {
			
			Pessoa pessoa = new Pessoa();
			pessoa.setNome(nome);
			
			/*Estamos configurando a pesquisa para consultar por partes do nome no banco de dados igual a like no SQL*/
			ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
				      .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
			
			/*Une o objeto com o valor e a configuração para consultar*/
			Example<Pessoa> example = Example.of(pessoa, ignoringExampleMatcher);
			
			Page<Pessoa> pessoas = findAll(example, pageable);
			
			return pessoas;
			
		}
	
	default Page<Pessoa> findPessoaBySexoPage(String nome,String sexo, Pageable pageable) {
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSexopessoa(sexo);
		
		/*Estamos configurando a pesquisa para consultar por partes do nome no banco de dados igual a like no SQL*/
		ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
			      .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			      .withMatcher("sexopessoa", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		/*Une o objeto com o valor e a configuração para consultar*/
		Example<Pessoa> example = Example.of(pessoa, ignoringExampleMatcher);
		
		Page<Pessoa> pessoas = findAll(example, pageable);
		
		return pessoas;
		
	}
	
}
