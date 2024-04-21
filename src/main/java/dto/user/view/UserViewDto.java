package dto.user.view;

public record UserViewDto(Long idUser,
                          String name,
                          String phone,
                          String email,
                          String password) {
}
