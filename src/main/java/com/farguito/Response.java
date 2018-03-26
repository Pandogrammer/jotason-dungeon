package com.farguito;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Response extends LinkedHashMap<String, Object> {

	public void agregarMensaje(String mensaje) {
		if(mensaje != null) {
			if(this.containsKey("mensajes")) {
				Map<String, String> mensajes = (Map) get("mensajes");
				mensajes.put(String.valueOf(mensajes.size()), mensaje);
			} else {
				Map<String, String> mensajes = new LinkedHashMap();
				mensajes.put(String.valueOf(mensajes.size()), mensaje);
				this.put("mensajes", mensajes);
			}
		}
	}
	

	public void agregarMensaje(List<String> entrada) {
		if(entrada != null) {
			if(this.containsKey("mensajes")) {
				Map<String, String> mensajes = (Map) get("mensajes");
				entrada.stream().forEach(m -> {
					mensajes.put(String.valueOf(mensajes.size()), m);
				});
			} else {
				Map<String, String> mensajes = new LinkedHashMap();
				entrada.stream().forEach(m -> {
					mensajes.put(String.valueOf(mensajes.size()), m);
				});
				this.put("mensajes", mensajes);
			}
		}
	}
}
