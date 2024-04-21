package dto.user.controller;

public record UserUpdatePasswordControllerDto(String phone, String oldPassword, String newPassword) {
}
