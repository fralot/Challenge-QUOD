from flask import Flask, request, jsonify
from deepface import DeepFace
import base64
import cv2
import numpy as np
import io
from PIL import Image

app = Flask(__name__)

def decode_base64_image(base64_str):
    img_data = base64.b64decode(base64_str)
    img = Image.open(io.BytesIO(img_data))
    return cv2.cvtColor(np.array(img), cv2.COLOR_RGB2BGR)

@app.route("/compare", methods=["POST"])
def compare_faces():
    try:
        data = request.get_json()
        img1 = decode_base64_image(data["document"])
        img2 = decode_base64_image(data["selfie"])

        result = DeepFace.verify(img1_path=img1, img2_path=img2, model_name="Facenet")

        return jsonify({
            "verified": result["verified"],
            "distance": result["distance"]
        })

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001)
