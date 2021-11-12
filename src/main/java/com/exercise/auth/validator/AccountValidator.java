package com.exercise.auth.validator;

import com.exercise.auth.dto.user.UserJson;
import com.exercise.auth.model.Account;
import com.exercise.auth.model.Role;
import com.exercise.auth.repository.AccountRepository;
import com.exercise.auth.repository.RoleRepository;
import com.exercise.auth.util.enums.EnumUtil;
import com.exercise.auth.util.validator.ParameterMap;
import com.exercise.auth.util.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountValidator extends Validator<Optional<Account>> {

    private final AccountRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bcryptEncoder;

    @Override
    protected Optional<Account> doValidate(ParameterMap body) {
        // 1 Instantiate object with Optional.ofNullable() and get from body (used in update) or return new Object
        final Account account = Optional.ofNullable(
                (Account) body.get("sample")
        ).orElse(Account.builder().build());

        // 2 Create Optional Object
        final Optional<Account> accountOptional;

        // 3 Call overridden validateFields method
        validateFields(body);

        // Step 4 inside validateFields

        // 5 Create method build(T object, ParameterMap body)

        // Step 6 inside build

        // 7 Set value of Optional Object to build
        accountOptional = Optional.of(build(account, body));

        // 8 Return Optional Object
        return accountOptional;
    }

    @Override
    protected void validateFields(ParameterMap body) {
        body.validate("Username")
                .required("Required")
                .condition(value -> repository.findByUsername(value).isEmpty(), "Already Exists");

        body.validate("Password")
                .required("Required");

        body.validate("Email")
                .isEmail("Invalid Email Format");

        body.validate("Mobile")
                .isNumber("Invalid Mobile Format");

//        Optional.ofNullable(EnumUtil.search(Role.Code.class, (String) role))

        final UserJson userJson = UserJson.fromContext();

        body.validate("Roles")
                .condition((fieldValue) -> {
                    AtomicBoolean hasError = new AtomicBoolean(false);
                    new ArrayList<>(Arrays.asList(fieldValue.replaceAll("\\[", "")
                            .replaceAll("]", "").split(",")))
                            .forEach(role -> {
                                final Optional<Role.Code> codeOptional = Optional.ofNullable(
                                        EnumUtil.search(Role.Code.class, role.trim())
                                );
                                if (codeOptional.isPresent() && codeOptional.get().getId() < userJson.getRoles().get(0).getId()) {
                                    hasError.set(true);
                                }
                            });
                    return !hasError.get();
                }, "Insufficient Permission")
                .condition((fieldValue) -> {
                    AtomicBoolean hasError = new AtomicBoolean(false);
                    new ArrayList<>(Arrays.asList(fieldValue.replaceAll("\\[", "")
                            .replaceAll("]", "").split(",")))
                            .forEach(role -> {
                                System.out.println("Role : " + role.trim());
                                if (Optional.ofNullable(EnumUtil.search(Role.Code.class, role.trim())).isEmpty()) {
                                    hasError.set(true);
                                }
                            });
                    return !hasError.get();
                }, "Invalid Role")
                .required("Required");
    }

    protected Account build(final Account account, final ParameterMap body) {
        // 6 Set values to T
        account.setUsername(body.getString("Username"));
        account.setPassword(bcryptEncoder.encode(body.getString("Password")));
        account.setEmail(body.getString("Email"));
        account.setMobile(body.getString("Mobile"));

        final List<Role> roles = new ArrayList<>();
        new ArrayList<>(Arrays.asList(body.getString("Roles")
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .split(",")))
                .forEach(
                        role -> Optional.ofNullable(EnumUtil.search(Role.Code.class, role.trim()))
                                .ifPresent(code -> roles.add(new Role(code)))
                );
        account.setRole(roles);

        return account;
    }
}
