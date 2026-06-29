# Guía de Despliegue y Testeo de Microservicios en Kubernetes

Este documento detalla los pasos seguidos para el despliegue, configuración y testeo de la comunicación entre los microservicios `ms-productos` y `ms-pedidos`.

## 1. Testeo Local (Sin Docker / Sin Kubernetes)

Antes de contenerizar la aplicación, se verificó la correcta comunicación entre los microservicios utilizando `WebClient` de manera local.

**Pasos:**
1. Iniciar `ms-productos` (`puerto 8081`).
2. Iniciar `ms-pedidos` (`puerto 8082`).
3. Enviar una petición `POST` a `http://localhost:8082/api/pedidos` con el siguiente JSON:
```json
{
  "productId": 1,
  "quantity": 2
}
```
4. **Resultado:** El microservicio de pedidos se comunica exitosamente con el de productos, verifica el stock, lo descuenta y guarda el pedido en la base de datos de Supabase.

---

## 2. Construcción y Publicación de Imágenes Docker

Una vez validado el código, se construyeron las imágenes Docker y se subieron a Docker Hub.

**Comandos ejecutados:**
```powershell
# Para ms-productos
cd ms-productos
docker build -t llampier2/ms-productos:latest .
docker push llampier2/ms-productos:latest

# Para ms-pedidos
cd ../ms-pedidos
docker build -t llampier2/ms-pedidos:latest .
docker push llampier2/ms-pedidos:latest
```

---

## 3. Despliegue en Kubernetes

Con las imágenes listas en Docker Hub, se procedió a desplegar la infraestructura en un clúster de Kubernetes (utilizando Docker Desktop).

### 3.1. Recursos Utilizados
- **Namespace:** Entorno aislado llamado `microservicios`.
- **ConfigMap:** Almacena variables de entorno (URL del Supabase Session Pooler y URL interna del servicio de productos).
- **Secret:** Almacena credenciales sensibles de la base de datos codificadas en Base64.
- **Deployments:** Despliega 2 réplicas (Pods) por cada microservicio, con comprobaciones de estado (`livenessProbe` y `readinessProbe`) y límites de recursos (CPU/Memoria).
- **Services (ClusterIP):** Permite la comunicación interna DNS entre los microservicios (ej. `http://ms-productos-svc:8081`).
- **Services (LoadBalancer):** Expone los microservicios hacia el exterior.

### 3.2. Ejecución de Comandos de Despliegue
Se aplicaron los manifiestos YAML en el siguiente orden estricto:

```powershell
# 1. Crear el Namespace
kubectl apply -f k8s/namespace.yaml

# 2. Aplicar configuraciones y credenciales
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

# 3. Desplegar ms-productos (Deployment, Service, LoadBalancer)
kubectl apply -f k8s/ms-productos/

# 4. Desplegar ms-pedidos (Deployment, Service, LoadBalancer)
kubectl apply -f k8s/ms-pedidos/
```

### 3.3. Comandos de Verificación
Para validar que el despliegue fue exitoso:
```powershell
# Verificar estado de los Pods (deben estar en 1/1 Running)
kubectl get pods -n microservicios

# Verificar los Deployments
kubectl get deployments -n microservicios

# Verificar los Services y obtener las IPs de los LoadBalancers
kubectl get services -n microservicios

# Ver todos los recursos generados
kubectl get all -n microservicios
```

---

## 4. Testeo de la Comunicación en Kubernetes

Dado que en entornos locales (como Docker Desktop K8s) el LoadBalancer puede quedar en estado `<pending>`, se utiliza `port-forward` para redirigir el tráfico y realizar el testeo.

**Terminal 1 (Exponer productos):**
```powershell
kubectl port-forward svc/ms-productos-svc 8081:8081 -n microservicios
```

**Terminal 2 (Exponer pedidos):**
```powershell
kubectl port-forward svc/ms-pedidos-svc 8082:8082 -n microservicios
```

**Terminal 3 (o Postman): Test de Comunicación**
1. **Ver inventario inicial:** `GET http://localhost:8081/api/productos`
2. **Crear un pedido:** `POST http://localhost:8082/api/pedidos` (Verificando la respuesta JSON 201 Created).
3. **Ver inventario final:** `GET http://localhost:8081/api/productos/1` (Verificando que el stock disminuyó correctamente).

---

## 5. Conclusiones

1. **Resolución de Nombres (DNS Interno):** En Kubernetes, la comunicación entre microservicios no usa `localhost`, sino el nombre del servicio interno. En este caso, `ms-pedidos` se comunica con `ms-productos` a través del Service ClusterIP `http://ms-productos-svc:8081`.
2. **Desacoplamiento de Configuración:** El uso de `ConfigMap` y `Secret` permitió inyectar credenciales (como el Session Pooler de Supabase) y URLs dinámicas sin necesidad de alterar el código fuente ni reconstruir las imágenes de Docker.
3. **Escalabilidad y Disponibilidad:** Al usar `Deployments` con 2 réplicas y health checks (`liveness` y `readiness`), Kubernetes garantiza que la aplicación siga disponible aunque un pod falle, distribuyendo el tráfico eficientemente.
4. **Exposición controlada:** El servicio `LoadBalancer` permite a clientes externos consumir las APIs, mientras que los `ClusterIP` mantienen la comunicación entre microservicios de forma privada y segura dentro del clúster.
