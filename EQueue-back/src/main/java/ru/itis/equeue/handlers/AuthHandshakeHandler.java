package ru.itis.equeue.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.WebUtils;
import ru.itis.equeue.security.providers.JwtTokenProvider;

import javax.servlet.http.Cookie;
import java.util.Map;

@Component
public class AuthHandshakeHandler implements HandshakeHandler {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private DefaultHandshakeHandler handshakeHandler = new DefaultHandshakeHandler();

    @Override
    public boolean doHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws HandshakeFailureException {
        ServletServerHttpRequest request = (ServletServerHttpRequest)serverHttpRequest;
        Cookie cookie = WebUtils.getCookie(request.getServletRequest(), "AUTH");
        String token = "";
        if (cookie != null) {
            token = cookie.getValue();
        }
        if (jwtTokenProvider.validateToken(token)) {
            return handshakeHandler.doHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, map);
        } else {
            serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }


    }
}