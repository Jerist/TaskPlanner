package dto.user.view;

public record UserUpdatePasswordViewDto(String phone, String oldPassword, String newPassword) {
}
