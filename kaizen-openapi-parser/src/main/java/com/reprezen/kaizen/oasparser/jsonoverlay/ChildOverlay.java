/*******************************************************************************
 *  Copyright (c) 2017 ModelSolv, Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *     ModelSolv, Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package com.reprezen.kaizen.oasparser.jsonoverlay;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;

public class ChildOverlay<V, OV extends JsonOverlay<V>> implements IJsonOverlay<V> {

	private JsonPath path;
	private JsonOverlay<V> overlay;
	private JsonOverlay<?> parent;
	private Reference reference = null;

	public ChildOverlay(String path, V value, JsonOverlay<?> parent, OverlayFactory<V, OV> factory,
			ReferenceRegistry refReg) {
		this.path = new JsonPath(path);
		this.parent = parent;
		this.overlay = factory.create(value, parent, refReg);
	}

	public ChildOverlay(String path, JsonNode json, JsonOverlay<?> parent, OverlayFactory<V, OV> factory,
			ReferenceRegistry refReg) {
		this.path = new JsonPath(path);
		this.parent = parent;
		if (isReferenceNode(json)) {
			this.reference = refReg.getRef(json);
			JsonNode resolved = reference.resolve();
			if (reference.isValid()) {
				if (refReg.hasOverlay(resolved)) {
					JsonOverlay<?> overlay = refReg.getOverlay(resolved);
					if (factory.isCompatible(overlay)) {
						@SuppressWarnings("unchecked")
						JsonOverlay<V> castOverlay = (JsonOverlay<V>) overlay;
						this.overlay = castOverlay;
					} else {
						// TODO don't make this a parse killer - it's really a model error
						throw new IllegalStateException("Referenced object is not compatible with referencing site");
					}
				} else {
					// note - since this is a reference, we don't set parent. If there's a way to
					// navigate to the object directly, that will determine its parent
					this.overlay = factory.create(resolved, null, refReg);
					refReg.setOverlay(resolved, overlay);
				}
			} else {
				this.overlay = null;
			}
		} else {
			this.overlay = factory.create(json, parent, refReg);
		}
	}

	private boolean isReferenceNode(JsonNode node) {
		return node.isObject() && node.has("$ref");
	}

	public boolean isPresent() {
		return overlay.isPresent();
	}

	public V get() {
		return overlay.get();
	}

	public IJsonOverlay<?> find(JsonPointer path) {
		return overlay.find(path);
	}

	public IJsonOverlay<?> find(String path) {
		return overlay.find(path);
	}

	public void set(V value) {
		overlay.set(value);
	}

	public IJsonOverlay<?> getParent() {
		// Note: here we return the creator of the childnode, which for a reference is
		// the holder of the reference. This may not be the same as the parent of the
		// referenced object, which is available via getOverlay().getParent().
		return parent;
	}

	public IJsonOverlay<?> getRoot() {
		return parent != null ? parent.getParent() : overlay.getRoot();
	}

	public JsonNode toJson() {
		return overlay.toJson();
	}

	public JsonNode toJson(boolean keepEmpty) {
		return overlay.toJson(keepEmpty);
	}

	public JsonPath getPath() {
		return path;
	}

	public Reference getReference() {
		return reference;
	}

	public JsonOverlay<V> getOverlay() {
		return overlay;
	}

	@Override
	public String toString() {
		String refString = reference != null ? String.format("<%s>", reference.getRefString()) : "";
		return String.format("Child@%s%s: %s]", path, refString, overlay);
	}
}