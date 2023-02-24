package dev.xamet.dreamgamesstudy.utility;

import com.auth0.jwt.algorithms.Algorithm;

interface TokenUtil {
    Algorithm algo = Algorithm.HMAC256("secret".getBytes());
}
