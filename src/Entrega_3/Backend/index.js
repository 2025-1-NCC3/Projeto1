const express = require("express");
const app = express();
const cors = require("cors");

const motoristaRoutes = require("./routes/motorista");
const passageiroRoutes = require("./routes/passageiro");
const viagemRoutes = require("./routes/viagem");
const loginRoute = require("./routes/login");

const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

// Rotas
app.use("/motorista", motoristaRoutes);
app.use("/passageiro", passageiroRoutes);
app.use("/viagem", viagemRoutes);
app.use("/login", loginRoute);

// Rota raiz
app.get("/", (req, res) => res.send("🚀 Servidor rodando corretamente"));

// Inicia o servidor
app.listen(PORT, "0.0.0.0", () => {
  console.log(`🚀 Servidor rodando na porta ${PORT}`);
});
