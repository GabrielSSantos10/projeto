package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.UsuarioBO;
import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import br.edu.ifg.luziania.model.entity.Usuario;
import br.edu.ifg.luziania.model.log.LogService;
import br.edu.ifg.luziania.model.log.LogType;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;


import java.util.List;
import java.util.Optional;

@Path("/site_admin")
public class SiteAdminController {

    @Inject
    Template siteAdmin;
    @Inject
    Template conta;
    @Inject
    Template usuarios;
    @Inject
    Template editarUsuario;
    @Inject
    Template cadastroUsuario;
   @Inject
    JsonWebToken jwt;
    @Inject
    UsuarioBO usuarioBO;
    @Inject
    LogService logService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance carregarSiteAdmin() {
        String perfil = (jwt.getGroups() != null && !jwt.getGroups().isEmpty())
                ? jwt.getGroups().stream().findFirst().orElse("sem-perfil")
                : "sem-perfil";

        return siteAdmin.data("perfil", perfil);
    }

    @GET
    @Path("/conta")
    public TemplateInstance getContaPage() {
        return conta.instance();
    }

    @GET
    @Path("/cadastrarUsuarioPage")
    public TemplateInstance getCadastroUser() {
        return cadastroUsuario.instance();
    }

    @POST
    @Path("/cadastrarUsuario")
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrarUsuario(UsuarioDTO usuarioDTO) {
        try {

            Usuario usuarioCadastrado = usuarioBO.cadastrarUsuario(usuarioDTO);

            logService.registerLog(usuarioCadastrado.getId(), LogType.ACCESS, "Usuario cadastrado: " + usuarioCadastrado.getUsername());

            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity
                    ("Erro ao cadastrar usuário").build();
        }
    }

    // Método que atualizar os dados do usuario
    @PUT
    @Path("/atualizar")
    @RolesAllowed({"admin", "atendente", "medico"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response atualizarUsuario(UsuarioDTO usuarioAtualizado) {
        String username = jwt.getClaim("sub");

        UsuarioDTO usuarioExistente = usuarioBO.buscarUsuarioPorUsername(username);

        if (usuarioExistente != null) {
            usuarioExistente.setNome(usuarioAtualizado.getNome());
            usuarioExistente.setPerfil(usuarioAtualizado.getPerfil());

            if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {

                usuarioExistente.setSenha(usuarioAtualizado.getSenha());
            }

            usuarioBO.atualizarUsuario(usuarioExistente);

            return Response.ok("Usuário Atualizado! " +
                    "Faça o login novamente para atualizar suas credenciais.").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado.").build();
        }
    }

    @GET
    @Path("/usuario_page")
    //@RolesAllowed("admin")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance userPage() {
        return usuarios.instance();
    }

    @GET
    @Path("/usuario_list")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioBO.listarTodosUsuarios();
        return Response.ok(usuarios).build();
    }


    @GET
    @Path("/editarUsuario")
    public TemplateInstance getUsuarioPage() {
        return editarUsuario.instance();
    }

    @PUT
    @Path("/atualizar_usuario")
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response atualizar(UsuarioDTO usuarioDTO) {
        usuarioBO.atualizarUsuarioPorId(usuarioDTO);
        return Response.ok().build();
    }

    @GET
    @Path("/usuario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed("admin")
    public Response buscarPorId(@PathParam("id") Long id) {
        Optional<Usuario> usuarioOpt = usuarioBO.buscarUsuarioPorId(id);

        if (usuarioOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Usuario usuario = usuarioOpt.get();
        UsuarioDTO dto = new UsuarioDTO(
                usuario.getNome(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getCpf(),
                null // Senha não é retornada por segurança
        );

        return Response.ok(dto).build();
    }

    @GET
    @Path("/pesquisar")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pesquisarUsuarios(@QueryParam("nome") String nome,
                                      @QueryParam("cpf") String cpf,
                                      @QueryParam("email") String email) {
        if ((nome == null || nome.isBlank()) &&
                (cpf == null || cpf.isBlank()) &&
                (email == null || email.isBlank())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Pelo menos um critério de pesquisa deve ser fornecido.")
                    .build();
        }

        List<Usuario> usuarios = usuarioBO.pesquisarUsuarios(nome, email, cpf);
        return Response.ok(usuarios).build();
    }

}
