package com.japan.compass.annotation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/css/**")
                .antMatchers("/js/**")
                .antMatchers("/images/**")
                .antMatchers("/favicon.*")
                .antMatchers("/h2-console/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // パスの認可設定 上から順に適応される
                .authorizeRequests(authorize -> authorize
                        // ユーザ側、管理側のログインパスへのアクセスを許可
                        .antMatchers("/user/answerer", "/admin/login", "/error").permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/user/**").hasRole("USER")
                        .antMatchers("/image/**").hasAnyRole("ADMIN", "USER")
                        // 上記以外のリクエストは全て認可拒否される 設定忘れを防ぐためのベストプラクティス
                        .anyRequest().denyAll()
                )

                // フォームログイン認証の設定
                .formLogin(form -> form
                        .loginPage("/user/answerer")// GET /user/answererでログインフォームの呼び出し Controllerを呼ぶ
                        .loginProcessingUrl("/login") // POST /loginでSpringSecurityのログイン処理が実行される Controller定義は呼ばれない
                        // ユーザ側はController、管理側はspring securityで遷移パスのハンドリング
                        .failureUrl("/admin/login?error")
                        .defaultSuccessUrl("/admin/top")
                        .usernameParameter("username") // usernameのパラメータでformからのデータを受ける
                        .passwordParameter("password") // passwordのパラメータでformからのデータを受ける
                        .permitAll()
                )

                // 認可ハンドリング設定
                .exceptionHandling(handler -> handler
                        // 認可拒否後の遷移パス
                        .accessDeniedPage("/error?denied")
                        // 未認証のユーザーからのアクセス拒否のハンドリング AuthenticationEntryPointの実装クラス
                        .authenticationEntryPoint(new AuthenticationRedirectHandler())
                        // 認証済のユーザーからのアクセス拒否のハンドリング AccessDeniedHandlerの実装クラス
                        // .accessDeniedHandler(new AccessDeniedRedirectHandler())
                )

                // logout設定
                .logout(logout -> logout
                        .logoutUrl("/logout")// logout処理のurl
                        .logoutSuccessUrl("/user/answerer") // logout成功時の遷移先
                        .permitAll()
                );
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
