package br.edu.ifg.luziania.config;

import br.edu.ifg.luziania.model.bo.UsuarioBO;
import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import br.edu.ifg.luziania.model.entity.Usuario;
import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class StartupBean {

    @Inject
    UsuarioBO usuarioBO;

    @Inject
    UsuarioDAO usuarioDAO;

    @PostConstruct
    @Transactional
    public void init() {
        String cpfPadrao = "00000000000"; // CPF fictício

        Usuario existente = usuarioDAO.buscarPorCpf(cpfPadrao);
        if (existente == null) {
            UsuarioDTO adminDTO = new UsuarioDTO();
            adminDTO.setNome("Administrador");
            adminDTO.setUsername("admin");
            adminDTO.setEmail("admin@admin.com");
            adminDTO.setCpf(cpfPadrao);

            usuarioBO.cadastrarAdmin(adminDTO);

            System.out.println("✅ Usuário admin criado automaticamente");
        } else {
            System.out.println("ℹ️ Usuário admin já existe, nenhum dado foi inserido.");
        }
    }
}
