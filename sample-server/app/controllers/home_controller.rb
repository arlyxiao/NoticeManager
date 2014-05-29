class HomeController < ApplicationController

  def index

    img_url = request.url + 'luffy.jpg'

    p img_url

    title = ''
    desc = ''
    other = ''

    render :json => {
      :img_url => img_url, 
      :title => title,
      :desc => desc,
      :other => other
    }
  end

end
