package org.dofacoma.spring.akka;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import static org.dofacoma.spring.akka.AkkaSpringExtension.SPRING_EXTENSION_PROVIDER;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@Component
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class SpringActorProducer {

    private final ActorSystem actorSystem;

    public ActorRef createActor(final Actor parent, final Class<? extends Actor> actorBeanClass) {
        final ActorName actorNameAnno = AnnotationUtils.findAnnotation(actorBeanClass, ActorName.class);

        if (actorNameAnno != null && StringUtils.isNotBlank(actorNameAnno.name())) {
            return parent.context().actorOf(
                    SPRING_EXTENSION_PROVIDER.get(parent.context().system()).props(actorBeanClass),
                    actorNameAnno.name());
        } else {
            return parent.context().actorOf(SPRING_EXTENSION_PROVIDER.get(parent.context().system()).props(actorBeanClass));
        }
    }

    public ActorRef createActor(final Class<? extends Actor> actorBeanClass) {
        final ActorName actorNameAnno = AnnotationUtils.findAnnotation(actorBeanClass, ActorName.class);

        if (actorNameAnno != null && StringUtils.isNotBlank(actorNameAnno.name())) {
            return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(actorBeanClass), actorNameAnno.name());
        } else {
            return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(actorBeanClass));
        }
    }

    public ActorRef createActor(final Actor parent, final Class<? extends Actor> actorBeanClass, final Object... args) {
        final ActorName actorNameAnno = AnnotationUtils.findAnnotation(actorBeanClass, ActorName.class);

        if (actorNameAnno != null && StringUtils.isNotBlank(actorNameAnno.name())) {
            return parent.context().actorOf(
                    SPRING_EXTENSION_PROVIDER.get(parent.context().system()).props(actorBeanClass, args),
                    actorNameAnno.name());
        } else {
            return parent.context()
                    .actorOf(SPRING_EXTENSION_PROVIDER.get(parent.context().system()).props(actorBeanClass, args));
        }
    }

    public ActorRef createActor(final Class<? extends Actor> actorBeanClass, final Object... args) {
        final ActorName actorNameAnno = AnnotationUtils.findAnnotation(actorBeanClass, ActorName.class);

        if (actorNameAnno != null && StringUtils.isNotBlank(actorNameAnno.name())) {
            return actorSystem
                    .actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(actorBeanClass, args), actorNameAnno.name());
        } else {
            return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(actorBeanClass, args));
        }
    }

    public ActorRef createActor(final String actorBeanName) {
        return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(actorBeanName));
    }

    public ActorRef createActor(final String actorBeanName, final Object... args) {
        return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(actorBeanName, args));
    }
}
