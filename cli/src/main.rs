use std::env;
use crate::api::get_repository_details::RepositoryDetail;

pub mod api;

#[tokio::main]
async fn main() -> Result<(), reqwest::Error> {


    let client = reqwest::Client::new();

    let resp2: RepositoryDetail = client.get("http://localhost:8077/repository/1")
        .header("Token", env::var("TOKEN").unwrap())
        .send()
        .await?
        .json()
        .await?;

    println!("body2 = {:?}", resp2);

    //let data = api::get_repository_details::get_repository_detail();

    //println!("body3 = {:?}", data);

    Ok(())
}
