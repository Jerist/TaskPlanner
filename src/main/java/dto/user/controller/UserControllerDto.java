package dto.user.controller;

public record UserControllerDto(Long idUser,
                                String name,
                                String phone,
                                String email,
                                String password) {
}
