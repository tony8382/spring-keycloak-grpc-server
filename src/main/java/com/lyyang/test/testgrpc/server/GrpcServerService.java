package com.lyyang.test.testgrpc.server;

import com.lyyang.test.testgrpc.jwt.JwtTokenProvider;
import com.lyyang.test.testgrpc.model.GreeterGrpc;
import com.lyyang.test.testgrpc.model.GreeterProto;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
public class GrpcServerService extends GreeterGrpc.GreeterImplBase {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    @Secured("ROLE_USER")
    public void sayHello(GreeterProto.HelloRequest request, StreamObserver<GreeterProto.HelloReply> responseObserver) {

        GreeterProto.HelloReply reply = GreeterProto.HelloReply.newBuilder().setMessage("Hello User ==> " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void sayHelloAdmin(GreeterProto.HelloRequest request, StreamObserver<GreeterProto.HelloReply> responseObserver) {
        GreeterProto.HelloReply reply = GreeterProto.HelloReply.newBuilder().setMessage("Hello Admin ==> " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void authenticate(GreeterProto.AuthRequest request, StreamObserver<GreeterProto.AuthReply> responseObserver) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        GreeterProto.AuthReply reply = GreeterProto.AuthReply.newBuilder()
                .setToken(jwtTokenProvider.createToken(authentication))
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
