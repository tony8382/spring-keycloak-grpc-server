package com.lyyang.test.testgrpc.grpc.server;

import com.lyyang.test.testgrpc.grpc.server.model.GreeterGrpc;
import com.lyyang.test.testgrpc.grpc.server.model.GreeterProto;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;

@Slf4j
@GrpcService
public class GrpcServerService extends GreeterGrpc.GreeterImplBase {

    @Autowired
    private AuthenticationManager authenticationManager;

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

}
